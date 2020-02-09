package dk.skrypalle.imbue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

class LinkProviderInfo<T> {

    private final Class<T> resultType;
    private final Class<?> targetType;
    private final Type[] argumentTypes;
    private final Class<? extends Annotation> scope;
    private final ThrowingFunction<T> instanceProvider;

    LinkProviderInfo(
            Class<T> resultType, Class<?> targetType,
            Type[] argumentTypes,
            Class<? extends Annotation> scope,
            ThrowingFunction<T> instanceProvider) {
        this.resultType = resultType;
        this.targetType = targetType;
        this.argumentTypes = argumentTypes;
        this.scope = scope;
        this.instanceProvider = instanceProvider;
    }

    Class<T> getResultType() {
        return resultType;
    }

    Class<?> getTargetType() {
        return targetType;
    }

    Type[] getArgumentTypes() {
        return argumentTypes;
    }

    Class<? extends Annotation> getScope() {
        return scope;
    }

    ThrowingFunction<T> getInstanceProvider() {
        return instanceProvider;
    }

}
