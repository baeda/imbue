package dk.skrypalle.imbue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dk.skrypalle.imbue.ReflectionUtils.isAssignableFrom;

public final class Imbue {

    /**
     * TODO JAVADOC.
     */
    public static <T> T findLink(Class<T> type) {
        return findLink(type, null);
    }

    /**
     * TODO JAVADOC.
     */
    public static <T> T findLink(Class<T> type, Type genericType) {
        var allLinks = findAllLinks(type, genericType);
        if (allLinks.isEmpty()) {
            throw new ImbueUnsatisfiedLinkError("unsatisfied link for type '%s'", type);
        }
        if (allLinks.size() != 1) {
            throw new ImbueAmbiguousLinkError(
                    "too many linkable types for type '%s': %s",
                    type,
                    allLinks
            );
        }

        return allLinks.get(0);
    }

    /**
     * TODO JAVADOC.
     */
    public static <T> List<T> findAllLinks(Class<T> type) {
        return findAllLinks(type, null);
    }

    /**
     * TODO JAVADOC.
     */
    public static <T> List<T> findAllLinks(Class<T> type, Type genericType) {
        if (isAssignableFrom(genericType, ParameterizedType.class)) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            if (isAssignableFrom(type, Supplier.class)) {
                return Collections.singletonList(findSupplierLink(parameterizedType));
            }
            if (isAssignableFrom(type, Iterable.class)) {
                return Collections.singletonList(findIterableLink(parameterizedType));
            }
        }

        return Discovery.getLeafClassesOf(type).stream()
                .filter(ReflectionUtils::isProperlyScoped)
                .map(Linker::newInstance)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * TODO JAVADOC.
     */
    public static void link(Object instance) {
        SetterLinker.link(instance);
        FieldLinker.link(instance);
    }

    @SuppressWarnings("unchecked")
    static <T> T findSupplierLink(ParameterizedType parameterizedType) {
        var actualTypeArgument = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        Supplier<Object> supplier = () -> Imbue.findLink(actualTypeArgument);
        return (T) supplier;
    }

    @SuppressWarnings("unchecked")
    static <T> T findIterableLink(ParameterizedType parameterizedType) {
        var actualTypeArgument = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        Iterable<T> iterable = () -> Imbue.findAllLinks(actualTypeArgument).iterator();
        return (T) iterable;
    }

    private Imbue() { /* static utility */ }

}
