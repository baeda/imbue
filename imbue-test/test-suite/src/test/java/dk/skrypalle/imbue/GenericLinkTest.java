package dk.skrypalle.imbue;

import dk.skrypalle.imbue.test.GenericStatic;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ImbueJunit5Extension.class)
class GenericLinkTest {

    @LinkProvider(Dependent.class)
    static Function<String, String> provideFunction(GenericStatic<Integer> intSingleton) {
        return s -> s + " ## " + intSingleton.get();
    }

    @Link
    private GenericStatic<String> stringGenericStatic;

    @Link
    private GenericStatic<Integer> integerGenericStatic;

    @Link
    private Supplier<GenericStatic<Integer>> lazyIntegerGenericSingleton;

    @Link
    private Iterable<GenericStatic<Integer>> multiIntegerGenericSingleton;

    @Link
    private Function<String, String> stringFunction;

    @Test
    void linking_generic_string_type__links_correct_implementation() {
        assertThat(stringGenericStatic)
                .isNotNull();

        assertThat(stringGenericStatic.get())
                .isEqualTo("hello generic world");
    }

    @Test
    void linking_generic_integer_type__links_correct_implementation() {
        assertThat(integerGenericStatic)
                .isNotNull();

        assertThat(integerGenericStatic.get())
                .isEqualTo(23);
    }

    @Test
    void lazy_linking_generic_type__links_correct_implementation() {
        assertThat(lazyIntegerGenericSingleton)
                .isNotNull();

        var singleton = lazyIntegerGenericSingleton.get();
        assertThat(singleton)
                .isNotNull()
                .isSameAs(integerGenericStatic);

        assertThat(singleton.get())
                .isEqualTo(23);
    }

    @Test
    void multi_linking_generic_type__links_correct_implementation() {
        assertThat(multiIntegerGenericSingleton)
                .isNotNull();

        var singletons = StreamSupport.stream(multiIntegerGenericSingleton.spliterator(), false)
                .collect(Collectors.toList());

        assertThat(singletons)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        var singleton = singletons.get(0);
        assertThat(singleton)
                .isNotNull()
                .isSameAs(integerGenericStatic);

        assertThat(singleton.get())
                .isEqualTo(23);
    }

    @Test
    void link_provided_generic_interface__is_linked_correctly() {
        var result = stringFunction.apply("hello world");

        assertThat(result)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo("hello world ## 23");
    }

}
