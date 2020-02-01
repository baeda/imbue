package dk.skrypalle.imbue;

final class Linker {

    private final ConstructorLinker constructorLinker;
    private final FieldLinker fieldLinker;
    private final SetterLinker setterLinker;

    Linker(Imbue imbue) {
        constructorLinker = new ConstructorLinker(imbue);
        fieldLinker = new FieldLinker(imbue);
        setterLinker = new SetterLinker(imbue);
    }

    <T> T newInstance(Class<T> type) {
        T instance = constructorLinker.newInstance(type);
        link(instance);
        return instance;
    }

    void link(Object instance) {
        setterLinker.link(instance);
        fieldLinker.link(instance);
    }

}
