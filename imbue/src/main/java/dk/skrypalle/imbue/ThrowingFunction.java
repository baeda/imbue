package dk.skrypalle.imbue;

@FunctionalInterface
interface ThrowingFunction<T> {

    T apply(Object target, Object[] args) throws Throwable;

}
