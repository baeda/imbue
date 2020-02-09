package dk.skrypalle.imbue;

final class ConstructorLinker {

    private final ScopeHandlerProvider scopeHandlerProvider;
    private final Instantiator instantiator;
    private final Discovery discovery;

    ConstructorLinker(
            ScopeHandlerProvider scopeHandlerProvider,
            Instantiator instantiator,
            Discovery discovery) {
        this.scopeHandlerProvider = scopeHandlerProvider;
        this.instantiator = instantiator;
        this.discovery = discovery;
    }

    <T> T newInstance(Class<T> concreteType) {
        var scopes = ReflectionUtils.findScopes(concreteType, discovery);
        if (scopes.isEmpty()) {
            throw new ImbueScopeError(
                    "no scope found on type '%s' - expected exactly one of %s",
                    concreteType,
                    discovery.getAllScopes()
            );
        }
        if (scopes.size() != 1) {
            throw new ImbueScopeError(
                    "too many scopes found on type '%s' - expected exactly one of %s",
                    concreteType,
                    discovery.getAllScopes()
            );
        }

        return scopeHandlerProvider.getHandler(scopes.get(0))
                .supplyInstance(concreteType, instantiator::newInstanceOrFail);
    }

}
