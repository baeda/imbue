package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Dependent;

@Dependent
class DependentNumberSupplier implements NumberSupplier {

    private DependentNumberSupplier() { /* static utility */ }

    @Override
    public Number getNumber() {
        return 1982;
    }

}
