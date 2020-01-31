package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Singleton;

import java.util.concurrent.atomic.AtomicReference;

@Singleton
public class SingletonStringHolder implements StringHolder {

    private final AtomicReference<String> stringRef = new AtomicReference<>();

    @Override
    public void setString(String text) {
        stringRef.set(text);
    }

    @Override
    public String getString() {
        return toString() + " text=" + stringRef.get();
    }

}
