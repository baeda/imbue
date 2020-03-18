package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

final class ReflectionUtils {

    static boolean isAssignableFrom(Type type, Class<?> baseClass) {
        if (type == null || baseClass == null) {
            return false;
        }

        var clazz = type instanceof Class<?>
                ? (Class<?>) type
                : type.getClass();
        return baseClass.isAssignableFrom(clazz) || clazz == baseClass;
    }

    static boolean isLinkable(AnnotatedElement element) {
        return element.isAnnotationPresent(Link.class);
    }

    static boolean isStatic(Member member) {
        return Modifier.isStatic(member.getModifiers());
    }

    static boolean isProperlyScoped(AnnotatedElement element, Discovery discovery) {
        int count = 0;
        for (Class<? extends Annotation> annotation : discovery.getAllScopes()) {
            if (element.isAnnotationPresent(annotation)) {
                count++;
            }
        }

        return count >= 1;
    }

    static List<Class<? extends Annotation>> findScopes(
            AnnotatedElement element,
            Discovery discovery) {
        return discovery.getAllScopes().stream()
                .filter(element::isAnnotationPresent)
                .collect(Collectors.toUnmodifiableList());
    }

    static Object[] collectArgs(Imbue imbue, Executable executable) {
        return collectArgs(imbue, executable.getGenericParameterTypes());
    }

    static Object[] collectArgs(Imbue imbue, Type[] types) {
        return Arrays.stream(types)
                .map(imbue::findLink)
                .toArray();
    }

    static <T> List<T> getAllMembers(Class<?> type, Function<Class<?>, T[]> mapper) {
        var result = new ArrayList<T>();
        var current = type;
        while (current != null) {
            try {
                Collections.addAll(result, mapper.apply(current));
            } catch (NoClassDefFoundError e) {
                // ignore
            }
            current = current.getSuperclass();
        }

        return result;
    }

    private ReflectionUtils() { /* static utility */ }

}
