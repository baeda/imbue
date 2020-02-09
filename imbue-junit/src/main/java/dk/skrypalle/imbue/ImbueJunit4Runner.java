package dk.skrypalle.imbue;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

/**
 * TODO JAVADOC.
 */
public class ImbueJunit4Runner extends BlockJUnit4ClassRunner {

    /**
     * TODO JAVADOC.
     */
    public ImbueJunit4Runner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    /**
     * TODO JAVADOC.
     */
    public ImbueJunit4Runner(TestClass testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Object createTest() throws Exception {
        var test = super.createTest();
        return new Imbue(true).link(test);
    }

}
