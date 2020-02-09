package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class FieldLinkProviderTest {

    static class TestClass {}

    @LinkProvider(Singleton.class)
    private static TestClass testClassProvider = new TestClass();

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
                .isSameAs(secondGet);
    }

}
