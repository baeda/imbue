package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static dk.skrypalle.imbue.ReflectionUtils.findScopes;
import static dk.skrypalle.imbue.ReflectionUtils.getAllMembers;
import static dk.skrypalle.imbue.ReflectionUtils.isAssignableFrom;
import static dk.skrypalle.imbue.ReflectionUtils.isLinkable;
import static dk.skrypalle.imbue.ReflectionUtils.isProperlyScoped;
import static dk.skrypalle.imbue.ReflectionUtils.isStatic;
import static org.assertj.core.api.Assertions.assertThat;

class ReflectionUtilsTest {

    interface TestInterface {}

    @Singleton
    static class ScopedTestClass implements TestInterface {

        void test() {}

    }

    static class UnscopedTestClass implements TestInterface {}

    @Link
    private TestInterface annotatedField;

    private TestInterface unannotatedField;

    private static TestInterface staticField;

    @Test
    void isAssignableFrom_interface_identity__is_true() {
        var result = isAssignableFrom(TestInterface.class, TestInterface.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void isAssignableFrom_class_identity__is_true() {
        var result = isAssignableFrom(ScopedTestClass.class, ScopedTestClass.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void isAssignableFrom_class_to_interface__is_true() {
        var result = isAssignableFrom(ScopedTestClass.class, TestInterface.class);

        assertThat(result)
                .isTrue();
    }

    @Test
    void isAssignableFrom_interface_to_class__is_false() {
        var result = isAssignableFrom(TestInterface.class, ScopedTestClass.class);

        assertThat(result)
                .isFalse();
    }

    @Test
    void isLinkable_annotatedField__returns_true() throws Exception {
        var result = isLinkable(getClass().getDeclaredField("annotatedField"));

        assertThat(result)
                .isTrue();
    }

    @Test
    void isLinkable_unannotatedField__returns_false() throws Exception {
        var result = isLinkable(getClass().getDeclaredField("unannotatedField"));

        assertThat(result)
                .isFalse();
    }

    @Test
    void isStatic_static_field__returns_true() throws Exception {
        var result = isStatic(getClass().getDeclaredField("staticField"));

        assertThat(result)
                .isTrue();
    }

    @Test
    void isStatic_instance_field__returns_false() throws Exception {
        var result = isStatic(getClass().getDeclaredField("annotatedField"));

        assertThat(result)
                .isFalse();
    }

    @Test
    void isProperlyScoped_scoped_class__returns_true() {
        var result = isProperlyScoped(ScopedTestClass.class, discovery());

        assertThat(result)
                .isTrue();
    }

    @Test
    void isProperlyScoped_unscoped_class__returns_false() {
        var result = isProperlyScoped(UnscopedTestClass.class, discovery());

        assertThat(result)
                .isFalse();
    }

    @Test
    void findScopes_scoped_class__returns_list_of_Singleton() {
        var result = findScopes(ScopedTestClass.class, discovery());

        assertThat(result)
                .containsExactly(Singleton.class);
    }

    @Test
    void findScopes_unscoped_class__returns_empty_list() {
        var result = findScopes(UnscopedTestClass.class, discovery());

        assertThat(result)
                .isEmpty();
    }

    @Test
    void getAllMembers_() {
        var methods = getAllMembers(ScopedTestClass.class, Class::getDeclaredMethods);

        assertThat(methods)
                .extracting(Method::toGenericString)
                .contains("void dk.skrypalle.imbue.ReflectionUtilsTest$ScopedTestClass.test()");
    }

    private static Discovery discovery() {
        return Discovery.select(TestDiscovery.class);
    }

}
