package dk.skrypalle.imbue;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class TestDiscovery implements Discovery {

    public TestDiscovery() { }

    @Override
    public List<Class<?>> getAllClasses() {
        return List.of();
    }

    @Override
    public List<Class<? extends Annotation>> getAllScopes() {
        return List.of(Singleton.class);
    }

    @Override
    public <T> List<Class<T>> getImplementationsOf(Type type) {
        throw new NotImplementedException("getImplementationsOf");
    }

    @Override
    public <T> List<Class<T>> getLeafClassesOf(Type type) {
        throw new NotImplementedException("getLeafClassesOf");
    }

    @Override
    public <T> List<LinkProviderInfo<T>> getProvidersFor(Type type) {
        throw new NotImplementedException("getProvidersFor");
    }

}
