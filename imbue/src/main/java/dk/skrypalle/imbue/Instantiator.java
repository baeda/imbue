package dk.skrypalle.imbue;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Instantiator {

    private final Imbue imbue;

    Instantiator(Imbue imbue) {
        this.imbue = imbue;
    }

    <T> T newInstanceOrFail(Class<T> type) {
        var constructorInfo = getConstructorInfo(type);
        var constructor = constructorInfo.getConstructor();
        var args = constructorInfo.getArgs();

        try {

            constructor.setAccessible(true);
            T instance = constructor.newInstance(args);
            return imbue.link(instance);

        } catch (ReflectiveOperationException e) {
            throw new ImbueLinkingError(e, "failed to link by constructor '%s'", constructor);
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
