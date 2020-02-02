package dk.skrypalle.imbue;

import dk.skrypalle.imbue.Logger.Level;

import java.util.function.Function;
import java.util.function.Supplier;

final class TimingUtil {

    static <T> T time(String description, Supplier<T> action) {
        return time(res -> description, action, LoggerFactory.getLogger(3), Level.INFO);
    }

    static <T> T time(String description, Supplier<T> action, Level level) {
        return time(res -> description, action, LoggerFactory.getLogger(3), level);
    }

    static <T> T time(String description, Supplier<T> action, Logger logger, Level level) {
        return time(res -> description, action, logger, level);
    }

    static <T> T time(Function<T, String> descriptionProvider, Supplier<T> action) {
        return time(descriptionProvider, action, LoggerFactory.getLogger(3), Level.INFO);
    }

    static <T> T time(Function<T, String> descriptionProvider, Supplier<T> action, Level level) {
        return time(descriptionProvider, action, LoggerFactory.getLogger(3), level);
    }

    static <T> T time(
            Function<T, String> descriptionProvider,
            Supplier<T> action,
            Logger logger,
            Level level) {
        long start = System.nanoTime();
        T result = action.get();
        long duration = System.nanoTime() - start;
        var description = descriptionProvider.apply(result);
        logger.log(
                level,
                null,
                "completed '%s' in %.2fms",
                description,
                duration / 1000000.0
        );
        return result;
    }

    private TimingUtil() { /* static utility */ }

}
