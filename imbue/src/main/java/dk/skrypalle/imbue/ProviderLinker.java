package dk.skrypalle.imbue;

import static dk.skrypalle.imbue.Utils.rethrow;

class ProviderLinker {

    private final Imbue imbue;
    private final ScopeHandlerProvider scopeHandlerProvider;

    ProviderLinker(Imbue imbue, ScopeHandlerProvider scopeHandlerProvider) {
        this.imbue = imbue;
        this.scopeHandlerProvider = scopeHandlerProvider;
    }

    <T> T newInstance(LinkProviderInfo<T> info) {
        var scope = info.getScope();
        var resultType = info.getResultType();
        var targetType = info.getTargetType();
        var argumentTypes = info.getArgumentTypes();
        var instanceProvider = info.getInstanceProvider();

        var target = targetType == null ? null : imbue.findLink(targetType);
        var args = ReflectionUtils.collectArgs(imbue, argumentTypes);

        var handler = scopeHandlerProvider.getHandler(scope);
        return handler.supplyInstance(
                resultType,
                type -> {
                    try {
                        return instanceProvider.apply(target, args);
                    } catch (Throwable t) {
                        return rethrow(t);
                    }
                }
        );
    }

}
