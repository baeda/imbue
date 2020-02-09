package dk.skrypalle.imbue;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

abstract class AbstractImbueErrorTest<T extends ImbueError> {

    private final Class<T> classUnderTest;
    private final String format = UUID.randomUUID() + " %s";
    private final Object[] args = {UUID.randomUUID().toString()};
    private final String expectedMessage = String.format(format, args);

    AbstractImbueErrorTest(Class<T> classUnderTest) {
        this.classUnderTest = classUnderTest;
    }

    @Test
    void getMessage_constructor_String__message_correctly() {
        var error = newInstance(expectedMessage);

        assertThat(error)
                .hasMessage(expectedMessage);
    }

    @Test
    void getMessage_constructor_String_ObjectArray__message_correctly() {
        var error = newInstance(format, args);

        assertThat(error)
                .hasMessage(expectedMessage);
    }

    @Test
    void getMessage_constructor_Throwable_String__sets_cause_and_message_correctly() {
        var cause = new Throwable();
        var error = newInstance(cause, expectedMessage);

        assertThat(error)
                .hasCause(cause)
                .hasMessage(expectedMessage);
    }

    @Test
    void getMessage_constructor_Throwable_String_ObjectArray__sets_cause_and_message_correctly() {
        var cause = new Throwable();
        var error = newInstance(cause, format, args);

        assertThat(error)
                .hasCause(cause)
                .hasMessage(expectedMessage);
    }

    private T newInstance(Object... args) {
        var argTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class<?>[]::new);
        try {
            return classUnderTest.getConstructor(argTypes).newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

}
