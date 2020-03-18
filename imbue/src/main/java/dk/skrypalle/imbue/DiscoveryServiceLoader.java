package dk.skrypalle.imbue;

import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.stream.Collectors;

final class DiscoveryServiceLoader {

    static Discovery load() {
        var serviceProviders = ServiceLoader
                .load(Discovery.class)
                .stream()
                .collect(Collectors.toList());
        return getOnly(serviceProviders, Discovery.class);
    }

    static <T extends Discovery> T select(Class<T> targetType) {
        var serviceProviders = ServiceLoader
                .load(Discovery.class)
                .stream()
                .filter(a -> a.type() == targetType)
                .collect(Collectors.toList());
        return getOnly(serviceProviders, targetType);
    }

    private static <T> T getOnly(List<Provider<Discovery>> serviceProviders, Class<T> targetType) {
        if (serviceProviders.isEmpty()) {
            throw new ImbueError("No implementation of '%s' found.", targetType);
        }
        if (serviceProviders.size() != 1) {
            throw new ImbueError("More than one implementation of '%s' found.", targetType);
        }

        @SuppressWarnings("unchecked")
        T t = (T) serviceProviders.get(0).get();
        return t;
    }

    private DiscoveryServiceLoader() { /* static utility */ }

}
