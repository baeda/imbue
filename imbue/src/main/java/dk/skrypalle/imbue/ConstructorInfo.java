package dk.skrypalle.imbue;

import java.lang.reflect.Constructor;

class ConstructorInfo<T> {

    private final Constructor<T> constructor;
    private final Object[] args;

    ConstructorInfo(Constructor<T> constructor, Object[] args) {
        this.constructor = constructor;
        this.args = args;
    }

    Class<T> getType() {
        return constructor.getDeclaringClass();
    }

    Constructor<T> getConstructor() {
        return constructor;
    }

    Object[] getArgs() {
        return args;
    }

}
