package dk.skrypalle.imbue;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * TODO JAVADOC.
 */
public class ImbueJunit5Extension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) {
        new Imbue().link(testInstance);
    }

}
