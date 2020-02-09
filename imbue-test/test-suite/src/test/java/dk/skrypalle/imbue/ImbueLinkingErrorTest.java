package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static dk.skrypalle.imbue.TestUtils.parameterize;
import static dk.skrypalle.imbue.TestUtils.wildcardType;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ImbueLinkingErrorTest {

    @Test
    void liking_iterable_with_undeterminable_generic_definition__throws_exception() {
        var iterableOfQuestionmark = parameterize(
                Iterable.class,
                wildcardType()
        );

        var error = catchThrowable(() -> imbue().findLink(iterableOfQuestionmark));

        assertThat(error)
                .isInstanceOf(ImbueLinkingError.class)
                .hasMessageContaining(iterableOfQuestionmark.toString());
    }

    @Test
    void liking_supplier_with_undeterminable_generic_definition__throws_exception() {
        var supplierOfQuestionmark = parameterize(
                Supplier.class,
                wildcardType()
        );

        var error = catchThrowable(() -> imbue().findLink(supplierOfQuestionmark));

        assertThat(error)
                .isInstanceOf(ImbueLinkingError.class)
                .hasMessageContaining(supplierOfQuestionmark.toString());
    }

}
