package dk.skrypalle.imbue;

import dk.skrypalle.imbue.test.SingleCustomScope;
import dk.skrypalle.imbue.test.SingleDependent;
import dk.skrypalle.imbue.test.SingleSingleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class ScopeTest {

    @Link
    private Supplier<SingleDependent> singleDependentSupplier;

    @Link
    private Supplier<SingleSingleton> singleSingletonSupplier;

    @Link
    private Supplier<SingleCustomScope> singleCustomScopeSupplier;

    @Test
    void dependentScope__is_properly_linked() {
        assertThat(singleDependentSupplier)
                .isNotNull();

        var firstGet = singleDependentSupplier.get();
        var secondGet = singleDependentSupplier.get();

        assertThat(firstGet)
                .isNotNull();
        assertThat(secondGet)
                .isNotNull();
        assertThat(firstGet)
                .isNotSameAs(secondGet);
    }

    @Test
    void singletonScope__is_properly_linked() {
        assertThat(singleSingletonSupplier)
                .isNotNull();

        var firstGet = singleSingletonSupplier.get();
        var secondGet = singleSingletonSupplier.get();

        assertThat(firstGet)
                .isNotNull();
        assertThat(secondGet)
                .isNotNull();
        assertThat(firstGet)
                .isSameAs(secondGet);
    }

    @Test
    void customScope__is_properly_linked() {
        assertThat(singleCustomScopeSupplier)
                .isNotNull();

        var firstGet = singleCustomScopeSupplier.get();
        var secondGet = singleCustomScopeSupplier.get();

        assertThat(firstGet)
                .isNotNull();
        assertThat(secondGet)
                .isNotNull();
        assertThat(firstGet)
                .isNotSameAs(secondGet);
    }

}
