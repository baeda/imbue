package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Singleton;

@Singleton
public class SingletonNumberSupplier implements NumberSupplier {

    @Override
    public Number getNumber() {
        return 2310;
    }

}
