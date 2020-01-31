package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Dependent;
import dk.skrypalle.imbue.Link;

@Dependent
public class NumberSupplierTestApp {

    private final Iterable<NumberSupplier> numberSuppliersByConstructor;

    private Iterable<NumberSupplier> numberSuppliersBySetter;

    @Link
    private Iterable<NumberSupplier> numberSuppliersByField;

    @Link
    public NumberSupplierTestApp(Iterable<NumberSupplier> numberSuppliersByConstructor) {
        this.numberSuppliersByConstructor = numberSuppliersByConstructor;
    }

    @Link
    public void setNumberSupplierBySetter(Iterable<NumberSupplier> numberSuppliersBySetter) {
        this.numberSuppliersBySetter = numberSuppliersBySetter;
    }

    public Iterable<NumberSupplier> getNumberSuppliersByConstructor() {
        return numberSuppliersByConstructor;
    }

    public Iterable<NumberSupplier> getNumberSuppliersBySetter() {
        return numberSuppliersBySetter;
    }

    public Iterable<NumberSupplier> getNumberSuppliersByField() {
        return numberSuppliersByField;
    }

}
