package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Dependent;
import dk.skrypalle.imbue.Link;

@Dependent
public class StringHolderTestApp {

    private final StringHolder stringHolderByConstructor;

    private StringHolder stringHolderBySetter;

    @Link
    private StringHolder stringHolderByField;

    @Link
    public StringHolderTestApp(StringHolder stringHolderByConstructor) {
        this.stringHolderByConstructor = stringHolderByConstructor;
    }

    @Link
    public void setStringHolderBySetter(StringHolder stringHolderBySetter) {
        this.stringHolderBySetter = stringHolderBySetter;
    }

    public StringHolder getStringHolderByConstructor() {
        return stringHolderByConstructor;
    }

    public StringHolder getStringHolderBySetter() {
        return stringHolderBySetter;
    }

    public StringHolder getStringHolderByField() {
        return stringHolderByField;
    }

}
