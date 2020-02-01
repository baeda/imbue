package dk.skrypalle.imbue;

import dk.skrypalle.imbue.ScopeHandler.HandlesScope;

@HandlesScope(Dependent.class)
public class DependentScopeHandler implements ScopeHandler {

    @Override
    public <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider) {
        return instanceProvider.newInstance(type);
    }

}
