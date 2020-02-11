package dk.skrypalle.imbue;

import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Type;

final class TestUtils {

    static Imbue imbue() {
        return new Imbue();
    }

    static Type parameterize(Class<?> rawType, Type... typeArguments) {
        return TypeUtils.parameterizeWithOwner(null, rawType, typeArguments);
    }

    static Type wildcardType() {
        return TypeUtils.wildcardType().build();
    }


    static boolean isAssignableFrom(Class<?> type, String baseClassName) {
        var baseClass = loadClass(baseClassName);
        return baseClass.isAssignableFrom(type);
    }

    private static Class<?> loadClass(String className) {
        try {

            return Class.forName(className);

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private TestUtils() { /* static utility */ }

}
