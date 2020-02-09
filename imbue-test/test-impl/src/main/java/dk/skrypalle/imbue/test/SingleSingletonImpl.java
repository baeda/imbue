package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Link;
import dk.skrypalle.imbue.Singleton;

import java.util.function.Supplier;

@Singleton
public class SingleSingletonImpl implements SingleSingleton {

    @Link
    private Supplier<SingleSingleton> self;

    @Override
    public SingleSingleton self() {
        return self.get();
    }

}
