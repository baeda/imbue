package dk.skrypalle.imbue;

import dk.skrypalle.imbue.test.SingleDependent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class MethodLinkProviderTest {

    static class TestClass {
        final SingleDependent singleDependent;

        TestClass(SingleDependent singleDependent) {
            this.singleDependent = singleDependent;
        }
    }

    @Singleton
    static class TestClassLinkProvider {
        @LinkProvider(Dependent.class)
        TestClass provide(SingleDependent singleDependent) {
            return new TestClass(singleDependent);
        }
    }

    @Link
    private Supplier<TestClass> unannotated;

    @Test
    void method_link_provider_is_linked_properly() {
        assertThat(unannotated)
                .isNotNull();

        var firstGet = unannotated.get();
        var secondGet = unannotated.get();

        assertThat(firstGet)
                .isNotNull();
        assertThat(secondGet)
                .isNotNull();

        assertThat(firstGet)
                .isNotSameAs(secondGet);

        assertThat(firstGet.singleDependent)
                .isNotNull();
        assertThat(secondGet.singleDependent)
                .isNotNull();

        assertThat(firstGet.singleDependent)
                .isNotSameAs(secondGet.singleDependent);
    }

}

