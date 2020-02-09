package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class TooManyViableScopesOnTypeTest {

    @Singleton
    @Dependent
    static class TestClass {}

    @Test
    void linking_with_more_than_one_scope_annotated__results_in_scope_error() {
        var error = catchThrowable(() -> imbue().findLink(TestClass.class));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueScopeError.class)
                .hasMessageContaining(Singleton.class.getCanonicalName());
    }

}
