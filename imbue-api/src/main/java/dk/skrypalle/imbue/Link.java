package dk.skrypalle.imbue;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO JAVADOC.
 */
@Documented
@Retention(RUNTIME)
@Target({CONSTRUCTOR, METHOD, FIELD})
public @interface Link {}
