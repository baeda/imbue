package dk.skrypalle.imbue;

import java.lang.reflect.Method;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class SetterLinker {

    static void link(Object instance) {
        var linkableSetters = Stream.of(instance.getClass().getDeclaredMethods())
                .filter(ReflectionUtils::isLinkable)
                .collect(Collectors.toList());

        for (Method setter : linkableSetters) {
            try {

                if (ReflectionUtils.isNotPublic(setter)) {
                    setter.setAccessible(true);
                }
                setter.invoke(instance, ReflectionUtils.collectArgs(setter));

            } catch (ReflectiveOperationException e) {
                throw new ImbueLinkingError(e, "failed to link by setter '%s'", setter);
            }
        }
    }

    private SetterLinker() { /* static utility */ }

}
