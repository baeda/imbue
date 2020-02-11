package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * TODO JAVADOC.
 */
public interface Discovery {

    /**
     * TODO JAVADOC.
     */
    static Discovery load() {
        return DiscoveryServiceLoader.load();
    }

    /**
     * TODO JAVADOC.
     */
    static <T extends Discovery> T select(Class<T> targetType) {
        return DiscoveryServiceLoader.select(targetType);
    }

    /**
     * TODO JAVADOC.
     */
    List<Class<?>> getAllClasses();

    /**
     * TODO JAVADOC.
     */
    List<Class<? extends Annotation>> getAllScopes();

    /**
     * TODO JAVADOC.
     */
    <T> List<Class<T>> getImplementationsOf(Type type);

    /**
     * TODO JAVADOC.
     */
    <T> List<Class<T>> getLeafClassesOf(Type type);

    /**
     * TODO JAVADOC.
     */
    <T> List<LinkProviderInfo<T>> getProvidersFor(Type type);

}
