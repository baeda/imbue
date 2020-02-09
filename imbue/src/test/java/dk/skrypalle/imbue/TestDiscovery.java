package dk.skrypalle.imbue;

import org.apache.commons.lang3.NotImplementedException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

class TestDiscovery extends Discovery {

    @Override
    List<Class<?>> getAllClasses() {
        return List.of();
    }

    @Override
    List<Class<? extends Annotation>> getAllScopes() {
        return List.of(Singleton.class);
    }

    @Override
    <T> List<Class<T>> getLeafClassesOf(Type type) {
        throw new NotImplementedException("getLeafClassesOf");
    }

    @Override
    <T> List<LinkProviderInfo<T>> getProvidersFor(Type type) {
        throw new NotImplementedException("getProvidersFor");
    }

}
