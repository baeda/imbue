package dk.skrypalle.imbue;

final class TestUtil {

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

    private TestUtil() { /* static utility */ }

}
