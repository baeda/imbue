package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO JAVADOC.
 */
public interface ScopeHandler {

    /**
     * TODO JAVADOC.
     */
    @Target(TYPE)
    @Retention(RUNTIME)
    @interface HandlesScope {
        Class<? extends Annotation> value();
    }

    /**
     * TODO JAVADOC.
     */
    interface InstanceProvider<T> {

        T newInstance(Class<T> type);

    }

    /**
     * TODO JAVADOC.
     */
    <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider);

}
