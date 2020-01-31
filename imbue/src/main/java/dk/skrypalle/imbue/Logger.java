package dk.skrypalle.imbue;

public interface Logger {

    enum Level {
        ERROR,
        WARN,
        INFO,
        CONFIG,
        DEBUG,
        TRACE
    }

    default void error(String message, Object... args) {
        log(Level.ERROR, null, message, args);
    }

    default void error(Throwable error, String message, Object... args) {
        log(Level.ERROR, error, message, args);
    }

    default void warn(String message, Object... args) {
        log(Level.WARN, null, message, args);
    }

    default void warn(Throwable error, String message, Object... args) {
        log(Level.WARN, error, message, args);
    }

    default void info(String message, Object... args) {
        log(Level.INFO, null, message, args);
    }

    default void info(Throwable error, String message, Object... args) {
        log(Level.INFO, error, message, args);
    }

    default void config(String message, Object... args) {
        log(Level.CONFIG, null, message, args);
    }

    default void config(Throwable error, String message, Object... args) {
        log(Level.CONFIG, error, message, args);
    }

    default void debug(String message, Object... args) {
        log(Level.DEBUG, null, message, args);
    }

    default void debug(Throwable error, String message, Object... args) {
        log(Level.DEBUG, error, message, args);
    }

    default void trace(String message, Object... args) {
        log(Level.TRACE, null, message, args);
    }

    default void trace(Throwable error, String message, Object... args) {
        log(Level.TRACE, error, message, args);
    }

    void log(Level level, Throwable error, String message, Object... args);

}
