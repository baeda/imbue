package dk.skrypalle.imbue;

import java.util.function.Supplier;
import java.util.logging.LogRecord;

final class LoggerFactory {

    static Logger getLogger() {
        return getLogger(3);
    }

    static Logger getLogger(int callDepth) {
        var loggerName = Thread.currentThread().getStackTrace()[callDepth].getClassName();
        return new DelegatingLogger(loggerName);
    }

    private static class DelegatingLogger implements Logger {
        private final String name;
        private final java.util.logging.Logger julLogger;

        DelegatingLogger(String name) {
            this.name = name;

            julLogger = java.util.logging.Logger.getLogger(name);
        }

        @Override
        public void log(Level level, Throwable error, String message, Object... args) {
            java.util.logging.Level julLevel = toJulLevel(level);
            logToJul(julLevel, error, () -> String.format(message, args));
        }

        private java.util.logging.Level toJulLevel(Level level) {
            switch (level) {
                case ERROR:
                    return java.util.logging.Level.SEVERE;
                case WARN:
                    return java.util.logging.Level.WARNING;
                case INFO:
                    return java.util.logging.Level.INFO;
                case CONFIG:
                    return java.util.logging.Level.CONFIG;
                case DEBUG:
                    return java.util.logging.Level.FINE;
                case TRACE:
                    return java.util.logging.Level.FINER;
                default:
                    throw new IllegalStateException("inexhaustive switch");
            }
        }

        private void logToJul(
                java.util.logging.Level level,
                Throwable throwable,
                Supplier<String> messageSupplier) {
            if (!julLogger.isLoggable(level)) {
                return;
            }

            var record = createLogRecord(level, throwable, messageSupplier);
            julLogger.log(record);
        }

        private LogRecord createLogRecord(
                java.util.logging.Level level,
                Throwable throwable,
                Supplier<String> messageSupplier) {
            String sourceClassName = null;
            String sourceMethodName = null;

            for (var element : Thread.currentThread().getStackTrace()) {
                String className = element.getClassName();
                if (name.equals(className)) {
                    sourceClassName = className;
                    sourceMethodName = element.getMethodName();
                    break;
                }
            }

            var message = messageSupplier != null
                    ? messageSupplier.get()
                    : null;
            var record = new LogRecord(level, message);
            record.setLoggerName(name);
            record.setThrown(throwable);
            record.setSourceClassName(sourceClassName);
            record.setSourceMethodName(sourceMethodName);
            record.setResourceBundleName(julLogger.getResourceBundleName());
            record.setResourceBundle(julLogger.getResourceBundle());
            return record;
        }
    }

    private LoggerFactory() { /* static utility */ }

}
