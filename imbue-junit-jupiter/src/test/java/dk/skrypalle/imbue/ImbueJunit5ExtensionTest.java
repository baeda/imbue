package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class ImbueJunit5ExtensionTest {

    @Singleton
    private static class LinkTest {}

    @Link
    private LinkTest fieldLinkTest;
    private LinkTest setterLinkTest;

    @Link
    public void setSetterLinkTest(LinkTest setterLinkTest) {
        this.setterLinkTest = setterLinkTest;
    }

    @Test
    public void link_by_field__is_properly_liked() {
        assertThat(fieldLinkTest)
                .isNotNull();
    }

    @Test
    public void link_by_setter__is_properly_liked() {
        assertThat(setterLinkTest)
                .isNotNull();
    }

}
