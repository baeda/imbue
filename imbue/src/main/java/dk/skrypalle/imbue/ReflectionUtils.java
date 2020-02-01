package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class ReflectionUtils {

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
        for (Class<? extends Annotation> annotation : Discovery.getAllScopes()) {
            if (element.isAnnotationPresent(annotation)) {
                count++;
            }
        }

        return count == 1;
    }

    static Class<? extends Annotation> findScope(AnnotatedElement element) {
        var scopes = Discovery.getAllScopes().stream()
                .filter(element::isAnnotationPresent)
                .collect(Collectors.toList());
        if (scopes.size() != 1) {
            return null;
        }

        return scopes.get(0);
    }

    static Object[] collectArgs(Imbue imbue, Executable executable) {
        var argsList = new ArrayList<>();
        for (Type type : executable.getGenericParameterTypes()) {
            if (isAssignableFrom(type, ParameterizedType.class)) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                var rawType = parameterizedType.getRawType();
                if (isAssignableFrom(rawType, Supplier.class)) {
                    argsList.add(imbue.findSupplierLink(parameterizedType));
                } else if (isAssignableFrom(rawType, Iterable.class)) {
                    argsList.add(imbue.findIterableLink(parameterizedType));
                }
            } else if (type instanceof Class<?>) {
                argsList.add(imbue.findLink((Class<?>) type));
            }
        }

        return argsList.toArray();
    }

    private ReflectionUtils() { /* static utility */ }

}
