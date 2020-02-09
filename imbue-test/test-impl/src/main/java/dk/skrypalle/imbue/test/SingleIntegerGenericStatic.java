package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Static;

@Static
class SingleIntegerGenericStatic implements GenericStatic<Integer> {

    @Override
    public Integer get() {
        return 23;
    }

}
