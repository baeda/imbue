package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SuppressWarnings("ResultOfMethodCallIgnored")
class UtilsTest {

    @Test
    public void rethrow__rethrows_checked_exception_without_compiler_complaining() {
        var exception = new IOException();
        var error = catchThrowable(() -> Utils.rethrow(exception));

        assertThat(error)
                .isSameAs(exception);
    }

    @Test
    public void uncheckedThrow__rethrows_checked_exception_without_compiler_complaining() {
        var exception = new IOException();
        var error = catchThrowable(() -> Utils.uncheckedThrow(exception));

        assertThat(error)
                .isSameAs(exception);
    }

}
