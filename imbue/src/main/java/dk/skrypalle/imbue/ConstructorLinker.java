package dk.skrypalle.imbue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ConstructorLinker {

    private final Imbue imbue;
    private final Map<Class<?>, Object> singletons;

    ConstructorLinker(Imbue imbue) {
        this.imbue = imbue;

        singletons = new ConcurrentHashMap<>();
    }

    <T> T newInstance(Class<T> concreteType) {
        var constructorInfo = getConstructorInfo(concreteType);
        var type = constructorInfo.getType();

        if (ReflectionUtils.isMoreThanOneScopePresent(type)) {
            throw new ImbueScopeError(
                    "type '%s' must be annotated with exactly one of %s",
                    type,
                    ReflectionUtils.getAllowedScopes()
            );
        }

        if (type.isAnnotationPresent(Dependent.class)) {
            return newInstanceOrFail(constructorInfo);
        }
        if (type.isAnnotationPresent(Singleton.class)) {
            @SuppressWarnings("unchecked")
            T instance = (T) singletons.computeIfAbsent(
                    type,
                    t -> newInstanceOrFail(constructorInfo)
            );
            return instance;
        }

        throw new ImbueScopeError(
                "type '%s' was eligible for linking but was not annotated with a proper scope (%s)",
                type,
                ReflectionUtils.getAllowedScopes()
        );
    }

    private static <T> T newInstanceOrFail(ConstructorInfo<T> constructorInfo) {
        var constructor = constructorInfo.getConstructor();
        var args = constructorInfo.getArgs();

        try {

            if (ReflectionUtils.isNotPublic(constructor)) {
                constructor.setAccessible(true);
            }

            return constructor.newInstance(args);

        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> ConstructorInfo<T> getConstructorInfo(Class<T> type) {
        Constructor<T> defaultConstructor = null;
        var linkableConstructors = new ArrayList<Constructor<T>>();
        for (var constructor : getDeclaredConstructors(type)) {
            if (ReflectionUtils.isLinkable(constructor)) {
                linkableConstructors.add(constructor);
            }
            if (constructor.getParameterCount() == 0) {
                defaultConstructor = constructor;
            }
        }

        if (linkableConstructors.isEmpty() && defaultConstructor != null) {
            return new ConstructorInfo<>(defaultConstructor, new Object[0]);
        }

        if (linkableConstructors.size() == 1) {
            var constructor = linkableConstructors.get(0);
            var args = ReflectionUtils.collectArgs(imbue, constructor);
            return new ConstructorInfo<>(constructor, args);
        }

        throw new IllegalStateException();
    }

    private <T> List<Constructor<T>> getDeclaredConstructors(Class<T> type) {
        return Stream.of(type.getDeclaredConstructors())
                .map(this::<T>asActualConstructor)
                .collect(Collectors.toList());
    }

    private <T> Constructor<T> asActualConstructor(Constructor<?> constructor) {
        @SuppressWarnings("unchecked")
        var cast = (Constructor<T>) constructor;
        return cast;
    }

}
