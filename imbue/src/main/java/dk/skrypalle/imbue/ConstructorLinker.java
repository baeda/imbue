package dk.skrypalle.imbue;

import dk.skrypalle.imbue.ScopeHandler.HandlesScope;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ConstructorLinker {

    private final Imbue imbue;
    private final Map<Class<? extends ScopeHandler>, ScopeHandler> scopeHandlers;

    ConstructorLinker(Imbue imbue) {
        this.imbue = imbue;

        scopeHandlers = new ConcurrentHashMap<>();
    }

    <T> T newInstance(Class<T> concreteType) {
        var scope = ReflectionUtils.findScope(concreteType);
        if (scope == null) {
            throw new ImbueScopeError(
                    "type '%s' must be annotated with exactly one of %s",
                    concreteType,
                    Discovery.getAllScopes()
            );
        }

        var scopeHandlers = Discovery.getLeafClassesOf(ScopeHandler.class).stream()
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
            throw new ImbueScopeError("no scope-handler found for scope '%s'", scope);
        }
        if (scopeHandlers.size() != 1) {
            throw new ImbueScopeError(
                    "more than one scope-handler found for scope '%s': %s",
                    scope,
                    scopeHandlers
            );
        }

        var scopeHandler = getScopeHandler(scopeHandlers.get(0));
        return scopeHandler.supplyInstance(concreteType, this::newInstanceOrFail);
    }

    private ScopeHandler getScopeHandler(Class<? extends ScopeHandler> scopeHandlerClass) {
        return scopeHandlers.computeIfAbsent(scopeHandlerClass, clazz -> {
            var constructorInfo = getConstructorInfo(clazz);
            var scopeHandler = newInstanceOrFail(constructorInfo);
            return imbue.link(scopeHandler);
        });
    }

    private <T> T newInstanceOrFail(Class<T> type) {
        return newInstanceOrFail(getConstructorInfo(type));
    }

    private static <T> T newInstanceOrFail(ConstructorInfo<T> constructorInfo) {
        var constructor = constructorInfo.getConstructor();
        var args = constructorInfo.getArgs();

        try {

            constructor.setAccessible(true);
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
