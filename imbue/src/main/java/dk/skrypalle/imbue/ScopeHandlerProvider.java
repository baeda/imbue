package dk.skrypalle.imbue;

import dk.skrypalle.imbue.ScopeHandler.HandlesScope;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class ScopeHandlerProvider {

    private final Instantiator instantiator;
    private final Discovery discovery;
    private final Map<Class<? extends ScopeHandler>, ScopeHandler> scopeHandlers;

    ScopeHandlerProvider(Instantiator instantiator, Discovery discovery) {
        this.instantiator = instantiator;
        this.discovery = discovery;
        scopeHandlers = new ConcurrentHashMap<>();
    }

    <T extends ScopeHandler> ScopeHandler getHandler(Class<? extends Annotation> scope) {
        var scopeHandlers = discovery.<T>getLeafClassesOf(ScopeHandler.class).stream()
                .filter(handlerClass -> {
                    var handles = handlerClass.getAnnotation(HandlesScope.class);
                    if (handles == null) {
                        throw new ImbueScopeError(
                                "scope-handler '%s' is not annotated with %s",
                                handlerClass,
                                HandlesScope.class
                        );
                    }
                    return handles.value() == scope;
                })
                .collect(Collectors.toList());

        if (scopeHandlers.isEmpty()) {
            throw new ImbueScopeError(
                    "no scope-handler found for scope '%s'",
                    scope.getCanonicalName()
            );
        }
        if (scopeHandlers.size() != 1) {
            throw new ImbueScopeError(
                    "more than one scope-handler found for scope '%s': %s",
                    scope.getCanonicalName(),
                    scopeHandlers
            );
        }

        return getScopeHandler(scopeHandlers.get(0));
    }

    private ScopeHandler getScopeHandler(Class<? extends ScopeHandler> scopeHandlerClass) {
        return scopeHandlers.computeIfAbsent(scopeHandlerClass, instantiator::newInstanceOrFail);
    }

}
