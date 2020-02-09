package dk.skrypalle.imbue.test;

import dk.skrypalle.imbue.Static;

@Static
class SingleStringGenericStatic implements GenericStatic<String> {

    @Override
    public String get() {
        return "hello generic world";
    }

}
