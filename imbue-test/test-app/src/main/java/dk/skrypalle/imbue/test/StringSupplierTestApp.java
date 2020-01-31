package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Dependent;
import dk.skrypalle.imbue.Link;

@Dependent
public class StringSupplierTestApp {

    private final StringSupplier stringSupplierByConstructor;

    private StringSupplier stringSupplierBySetter;

    @Link
    private StringSupplier stringSupplierByField;

    @Link
    public StringSupplierTestApp(StringSupplier stringSupplierByConstructor) {
        this.stringSupplierByConstructor = stringSupplierByConstructor;
    }

    @Link
    public void setStringSupplierBySetter(StringSupplier stringSupplierBySetter) {
        this.stringSupplierBySetter = stringSupplierBySetter;
    }

    public StringSupplier getStringSupplierByConstructor() {
        return stringSupplierByConstructor;
    }

    public StringSupplier getStringSupplierBySetter() {
        return stringSupplierBySetter;
    }

    public StringSupplier getStringSupplierByField() {
        return stringSupplierByField;
    }

}
