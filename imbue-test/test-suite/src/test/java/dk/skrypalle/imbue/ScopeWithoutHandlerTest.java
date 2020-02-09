package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ScopeWithoutHandlerTest {

    @Scope
    @Retention(RUNTIME)
    @interface ScopeAnnotationWithoutHandler {}

    @ScopeAnnotationWithoutHandler
    static class TestClass {}

    @Test
    void linking_with_no_scope_handler__results_in_scope_error() {
        var error = catchThrowable(() -> imbue().findLink(TestClass.class));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueScopeError.class)
                .hasMessageContaining(ScopeAnnotationWithoutHandler.class.getCanonicalName());
    }

}
