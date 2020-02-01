package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Scope;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Scope
@Target(TYPE)
@Retention(RUNTIME)
public @interface CustomScope {}
