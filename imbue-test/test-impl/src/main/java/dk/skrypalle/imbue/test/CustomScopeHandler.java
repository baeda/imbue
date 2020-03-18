package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.ScopeHandler;
import dk.skrypalle.imbue.ScopeHandler.HandlesScope;

@HandlesScope(CustomScope.class)
class CustomScopeHandler implements ScopeHandler {

    @Override
    public <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider) {
        return instanceProvider.newInstance(type);
    }

}
