package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.ImbueJunit5Extension;
import dk.skrypalle.imbue.Link;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
public class JunitJupiterTest {

    private static final Function<NumberSupplier, Number> NUMBERS_FROM_SUPPLIER
            = NumberSupplier::getNumber;

    @Link
    private StringHolderTestApp stringHolderTestApp;

    @Link
    private StringSupplierTestApp stringSupplierTestApp;

    @Link
    private NumberSupplierTestApp numberSupplierTestApp;

    @Test
    void singletonScope() {
        assertThat(stringHolderTestApp)
                .isNotNull();

        assertThat(stringHolderTestApp.getStringHolderByConstructor())
                .isNotNull()
                .isSameAs(stringHolderTestApp.getStringHolderBySetter())
                .isSameAs(stringHolderTestApp.getStringHolderByField());
    }

    @Test
    void dependentScope() {
        assertThat(stringSupplierTestApp)
                .isNotNull();

        assertThat(stringSupplierTestApp.getStringSupplierByConstructor())
                .isNotNull()
                .isNotSameAs(stringSupplierTestApp.getStringSupplierBySetter())
                .isNotSameAs(stringSupplierTestApp.getStringSupplierByField());
    }

    @Test
    void mixedScope_multiLink() {
        assertThat(numberSupplierTestApp)
                .isNotNull();

        assertThat(numberSupplierTestApp.getNumberSuppliersByConstructor())
                .isNotNull()
                .extracting(NUMBERS_FROM_SUPPLIER)
                .containsExactlyInAnyOrder(2310, 1982);
        assertThat(numberSupplierTestApp.getNumberSuppliersBySetter())
                .isNotNull()
                .extracting(NUMBERS_FROM_SUPPLIER)
                .containsExactlyInAnyOrder(2310, 1982);
        assertThat(numberSupplierTestApp.getNumberSuppliersByField())
                .isNotNull()
                .extracting(NUMBERS_FROM_SUPPLIER)
                .containsExactlyInAnyOrder(2310, 1982);
    }

}
