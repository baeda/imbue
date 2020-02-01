package dk.skrypalle.imbue;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class FieldLinker {

    private final Imbue imbue;

    FieldLinker(Imbue imbue) {
        this.imbue = imbue;
    }

    void link(Object instance) {
        var linkableFields = Stream.of(instance.getClass().getDeclaredFields())
                .filter(ReflectionUtils::isLinkable)
                .collect(Collectors.toList());

        for (Field field : linkableFields) {
            try {

                if (ReflectionUtils.isNotPublic(field)) {
                    field.setAccessible(true);
                }
                field.set(instance, imbue.findLink(field.getType(), field.getGenericType()));

            } catch (ReflectiveOperationException e) {
                throw new ImbueLinkingError(e, "failed to link by field '%s'", field);
            }
        }
    }

}
