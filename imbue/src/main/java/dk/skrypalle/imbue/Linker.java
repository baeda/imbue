package dk.skrypalle.imbue;

final class Linker {

    private final ConstructorLinker constructorLinker;
    private final ProviderLinker providerLinker;
    private final FieldLinker fieldLinker;
    private final SetterLinker setterLinker;

    Linker(Imbue imbue) {
        var instantiator = new Instantiator(imbue);
        var discovery = imbue.getDiscovery();
        var scopeHandlerProvider = new ScopeHandlerProvider(instantiator, discovery);

        constructorLinker = new ConstructorLinker(scopeHandlerProvider, instantiator, discovery);
        providerLinker = new ProviderLinker(imbue, scopeHandlerProvider);
        fieldLinker = new FieldLinker(imbue);
        setterLinker = new SetterLinker(imbue);
    }

    <T> T newInstance(Class<T> type) {
        return constructorLinker.newInstance(type);
    }

    <T> T newInstance(LinkProviderInfo<T> info) {
        return providerLinker.newInstance(info);
    }

    void link(Object instance) {
        setterLinker.link(instance);
        fieldLinker.link(instance);
    }

}
