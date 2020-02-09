package dk.skrypalle.imbue;

import dk.skrypalle.imbue.ScopeHandler.HandlesScope;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;

import static dk.skrypalle.imbue.TestUtils.imbue;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class ScopeWithMultipleHandlersTest {

    @Scope
    @Retention(RUNTIME)
    @interface ScopeAnnotationWithMultipleHandlers {}

    @HandlesScope(ScopeAnnotationWithMultipleHandlers.class)
    static class ScopeHandler1 implements ScopeHandler {
        @Override
        public <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider) {
            return instanceProvider.newInstance(type);
        }
    }

    @HandlesScope(ScopeAnnotationWithMultipleHandlers.class)
    static class ScopeHandler2 implements ScopeHandler {
        @Override
        public <T> T supplyInstance(Class<T> type, InstanceProvider<T> instanceProvider) {
            return instanceProvider.newInstance(type);
        }
    }

    @ScopeAnnotationWithMultipleHandlers
    static class TestClass {}

    @Test
    void linking_with_more_than_one_scope_handler__results_in_scope_error() {
        var error = catchThrowable(() -> imbue().findLink(TestClass.class));

        assertThat(error)
                .isNotNull()
                .isExactlyInstanceOf(ImbueScopeError.class)
                .hasMessageContaining(ScopeAnnotationWithMultipleHandlers.class.getCanonicalName());
    }

}
