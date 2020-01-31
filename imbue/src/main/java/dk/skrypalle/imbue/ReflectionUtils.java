package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

final class ReflectionUtils {

    private static final List<Class<? extends Annotation>> ALLOWED_SCOPES = List.of(
            Dependent.class,
            Singleton.class
    );

    private static final Logger log = LoggerFactory.getLogger();

    static List<Class<? extends Annotation>> getAllowedScopes() {
        return ALLOWED_SCOPES;
    }

    static boolean isNotPublic(Member member) {
        return !Modifier.isPublic(member.getModifiers());
    }

    static boolean isAssignableFrom(Type type, Class<?> baseClass) {
        if (type == null || baseClass == null) {
            return false;
        }

        var clazz = type instanceof Class<?>
                ? (Class<?>) type
                : type.getClass();
        return baseClass.isAssignableFrom(clazz);
    }

    static boolean isLinkable(AnnotatedElement element) {
        return element.isAnnotationPresent(Link.class);
    }

    static boolean isProperlyScoped(AnnotatedElement element) {
        int count = 0;
        for (Class<? extends Annotation> annotation : getAllowedScopes()) {
            if (element.isAnnotationPresent(annotation)) {
                count++;
            }
        }

        return count == 1;
    }

    static boolean isMoreThanOneScopePresent(AnnotatedElement element) {
        int count = 0;
        for (Class<? extends Annotation> annotation : getAllowedScopes()) {
            if (element.isAnnotationPresent(annotation)) {
                count++;
            }
        }

        return count > 1;
    }

    static Object[] collectArgs(Executable executable) {
        var argsList = new ArrayList<>();
        for (Type type : executable.getGenericParameterTypes()) {
            if (isAssignableFrom(type, ParameterizedType.class)) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                var rawType = parameterizedType.getRawType();
                if (isAssignableFrom(rawType, Supplier.class)) {
                    argsList.add(Imbue.findSupplierLink(parameterizedType));
                } else if (isAssignableFrom(rawType, Iterable.class)) {
                    argsList.add(Imbue.findIterableLink(parameterizedType));
                }
            } else if (type instanceof Class<?>) {
                argsList.add(Imbue.findLink((Class<?>) type));
            }
        }

        return argsList.toArray();
    }

    private ReflectionUtils() { /* static utility */ }

}
