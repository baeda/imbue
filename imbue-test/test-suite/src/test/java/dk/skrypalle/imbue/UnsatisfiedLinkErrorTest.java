package dk.skrypalle.imbue;

import dk.skrypalle.imbue.test.SingleSingleton;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UnsatisfiedLinkErrorTest {

    static class TestClass {}

    @Test
    void linking_without_scope_or_provider__results_in_unsatisfied_link_error() {
        var error = catchThrowable(() -> imbue().findLink(TestClass.class));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueUnsatisfiedLinkError.class)
                .hasMessageContaining(TestClass.class.toString());
    }

    @Test
    void linking_iterable_of_supplier__results_in_unsatisfied_link_error() {
        var iterableOfSupplier = TestUtils.parameterize(
                Iterable.class,
                TestUtils.parameterize(
                        Supplier.class,
                        SingleSingleton.class
                )
        );

        var error = catchThrowable(() -> imbue().findLink(iterableOfSupplier));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueUnsatisfiedLinkError.class)
                .hasMessageContaining(iterableOfSupplier.toString());
    }

}
