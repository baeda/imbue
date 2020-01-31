package dk.skrypalle.imbue;

final class Linker {

    static <T> T newInstance(Class<T> type) {
        T instance = ConstructorLinker.newInstance(type);
        Imbue.link(instance);
        return instance;
    }

    private Linker() { /* static utility */ }

}
