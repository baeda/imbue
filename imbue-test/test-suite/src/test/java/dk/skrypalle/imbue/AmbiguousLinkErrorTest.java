package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class AmbiguousLinkErrorTest {

    static class TestClass {}

    @LinkProvider(Dependent.class)
    static TestClass provideDependent() {
        return new TestClass();
    }

    @LinkProvider(Singleton.class)
    static TestClass provideSingleton() {
        return new TestClass();
    }

    @Test
    void linking_with_more_than_one_eligible_link__results_in_ambiguous_link_error() {
        var error = catchThrowable(() -> imbue().findLink(TestClass.class));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueAmbiguousLinkError.class)
                .hasMessageContaining(TestClass.class.toString());
    }

}
