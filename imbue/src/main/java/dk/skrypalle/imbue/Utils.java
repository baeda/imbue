package dk.skrypalle.imbue;

final class Utils {

    static <T> T rethrow(Throwable t) {
        uncheckedThrow(t);
        return null; // never reached - just to please the compiler
    }

    @SuppressWarnings("unchecked")
    static <T extends Throwable> void uncheckedThrow(Throwable t) throws T {
        if (t != null) {
            throw (T) t; // rely on vacuous cast
        } else {
            throw new Error("Unknown Exception");
        }
    }

    private Utils() { /* static utility */ }

}
