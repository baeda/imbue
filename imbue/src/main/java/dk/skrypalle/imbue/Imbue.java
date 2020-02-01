package dk.skrypalle.imbue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static dk.skrypalle.imbue.ReflectionUtils.isAssignableFrom;

public final class Imbue {

    private final Linker linker;

    public Imbue() {
        linker = new Linker(this);
    }

    /**
     * TODO JAVADOC.
     */
    public <T> T findLink(Class<T> type) {
        return findLink(type, null);
    }

    /**
     * TODO JAVADOC.
     */
    public <T> T findLink(Class<T> type, Type genericType) {
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
    public <T> List<T> findAllLinks(Class<T> type) {
        return findAllLinks(type, null);
    }

    /**
     * TODO JAVADOC.
     */
    public <T> List<T> findAllLinks(Class<T> type, Type genericType) {
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
                .map(linker::newInstance)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * TODO JAVADOC.
     */
    public <T> T link(T instance) {
        linker.link(instance);
        return instance;
    }

    @SuppressWarnings("unchecked")
    <T> T findSupplierLink(ParameterizedType parameterizedType) {
        var actualTypeArgument = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        Supplier<Object> supplier = () -> findLink(actualTypeArgument);
        return (T) supplier;
    }

    @SuppressWarnings("unchecked")
    <T> T findIterableLink(ParameterizedType parameterizedType) {
        var actualTypeArgument = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        Iterable<T> iterable = () -> findAllLinks(actualTypeArgument).iterator();
        return (T) iterable;
    }

}
