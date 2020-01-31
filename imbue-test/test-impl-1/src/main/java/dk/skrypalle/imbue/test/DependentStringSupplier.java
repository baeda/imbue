package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Dependent;

@Dependent
public class DependentStringSupplier implements StringSupplier {

    @Override
    public String getString() {
        return toString();
    }

}
