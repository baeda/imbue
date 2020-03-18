package dk.skrypalle.imbue;

import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

import static dk.skrypalle.imbue.LoggerFactory.getLogger;

final class TimingUtils {

    static <T> T time(String description, Supplier<T> action) {
        return time(res -> description, action, getLogger(3));
    }

    static <T> T time(String description, Supplier<T> action, Logger logger) {
        return time(res -> description, action, logger);
    }

    static <T> T time(Function<T, String> descriptionProvider, Supplier<T> action) {
        return time(descriptionProvider, action, getLogger(3));
    }

    static <T> T time(
            Function<T, String> descriptionProvider,
            Supplier<T> action,
            Logger logger) {
        long start = System.nanoTime();
        T result = action.get();
        if (!logger.isInfoEnabled()) {
            return result;
        }
        long duration = System.nanoTime() - start;
        var description = descriptionProvider.apply(result);
        var message = String.format("completed '%s' in %.2fms", description, duration / 1000000.0);
        logger.info(message);
        return result;
    }

    private TimingUtils() { /* static utility */ }

}
