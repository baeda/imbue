package dk.skrypalle.imbue;

import dk.skrypalle.imbue.ScopeHandler.HandlesScope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@HandlesScope(Static.class)
class StaticScopeHandler implements ScopeHandler {

    private static final Map<Class<?>, Object> singletons = new ConcurrentHashMap<>();

    @Override
    public <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider) {
        @SuppressWarnings("unchecked")
        T result = (T) singletons.computeIfAbsent(type, t -> instanceProvider.newInstance(type));
        return result;
    }

}
