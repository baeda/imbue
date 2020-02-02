package dk.skrypalle.imbue;

import dk.skrypalle.imbue.test.SingleSingleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class LinkTypeTest {

    @Dependent
    private static class LinkTypesHolder {
        private final SingleSingleton linkedByConstructor;

        private SingleSingleton linkedBySetter;

        @Link
        private SingleSingleton linkedByField;

        @Link
        private LinkTypesHolder(SingleSingleton linkedByConstructor) {
            this.linkedByConstructor = linkedByConstructor;
        }

        @Link
        private void setLinkedBySetter(SingleSingleton linkedBySetter) {
            this.linkedBySetter = linkedBySetter;
        }
    }

    @Link
    private LinkTypesHolder linkTypesHolder;

    @Test
    void by_constructor__is_linked_properly() {
        assertThat(linkTypesHolder)
                .isNotNull();

        assertThat(linkTypesHolder.linkedByConstructor)
                .isNotNull();
    }

    @Test
    void by_setter__is_linked_properly() {
        assertThat(linkTypesHolder)
                .isNotNull();

        assertThat(linkTypesHolder.linkedBySetter)
                .isNotNull();
    }

    @Test
    void by_field__is_linked_properly() {
        assertThat(linkTypesHolder)
                .isNotNull();

        assertThat(linkTypesHolder.linkedByField)
                .isNotNull();
    }

}
