package dk.skrypalle.imbue;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class ImbueJunit4Runner extends BlockJUnit4ClassRunner {

    public ImbueJunit4Runner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected Object createTest() throws Exception {
        return new Imbue().link(super.createTest());
    }

}
