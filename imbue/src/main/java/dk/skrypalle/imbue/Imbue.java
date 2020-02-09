package dk.skrypalle.imbue;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static dk.skrypalle.imbue.ReflectionUtils.isAssignableFrom;

/**
 * TODO JAVADOC.
 */
public final class Imbue {

    private static final Supplier<Discovery> discoveryRef = new Discovery.LazyInit();

    private final Linker linker;
    private final Discovery discovery;

    /**
     * TODO JAVADOC.
     */
    public Imbue() {
        this(false);
    }

    /**
     * TODO JAVADOC.
     */
    public Imbue(boolean staticDiscovery) {
        discovery = staticDiscovery
                ? discoveryRef.get()
                : new Discovery();

        linker = new Linker(this);
    }

    Discovery getDiscovery() {
        return discovery;
    }

    /**
     * TODO JAVADOC.
     */
    public <T> T findLink(Type type) {
        List<T> allLinks = findAllLinks(type);
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
    public <T> List<T> findAllLinks(Type type) {
        if (isAssignableFrom(type, ParameterizedType.class)) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            var raw = parameterizedType.getRawType();
            if (raw == Supplier.class) {
                return Collections.singletonList(findSupplierLink(parameterizedType));
            }
            if (raw == Iterable.class) {
                return Collections.singletonList(findIterableLink(parameterizedType));
            }
        }

        var result = new ArrayList<T>();
        discovery.<T>getLeafClassesOf(type)
                .stream()
                .filter(t -> ReflectionUtils.isProperlyScoped(t, discovery))
                .map(linker::newInstance)
                .forEach(result::add);
        discovery.<T>getProvidersFor(type)
                .stream()
                .map(linker::newInstance)
                .forEach(result::add);

        return List.copyOf(result);
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
        var actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class<?>) {
            Supplier<T> supplier = () -> findLink(actualTypeArgument);
            return (T) supplier;
        } else if (isAssignableFrom(actualTypeArgument, ParameterizedType.class)) {
            var nestedParameterizedType = (ParameterizedType) actualTypeArgument;
            Supplier<T> supplier = () -> findLink(nestedParameterizedType);
            return (T) supplier;
        }

        throw new ImbueLinkingError("failed to determine proper linkage of %s", parameterizedType);
    }

    @SuppressWarnings("unchecked")
    <T> T findIterableLink(ParameterizedType parameterizedType) {
        var actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class<?>) {
            Iterable<T> iterable = () -> this.<T>findAllLinks(actualTypeArgument).iterator();
            return (T) iterable;
        } else if (isAssignableFrom(actualTypeArgument, ParameterizedType.class)) {
            var nestedParameterizedType = (ParameterizedType) actualTypeArgument;
            var rawType = (Class<T>) nestedParameterizedType.getRawType();
            if (isAssignableFrom(rawType, Supplier.class)) {
                throw new ImbueUnsatisfiedLinkError("%s is not implemented", parameterizedType);
            }
            Iterable<T> iterable = () -> this.<T>findAllLinks(nestedParameterizedType).iterator();
            return (T) iterable;
        }

        throw new ImbueLinkingError("failed to determine proper linkage of %s", parameterizedType);
    }

}
