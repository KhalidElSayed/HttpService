
package novoda.lib.httpservice.util;

import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;

public class CustomRobolectricTestRunner extends RobolectricTestRunner {

    @SuppressWarnings("unchecked")
    public CustomRobolectricTestRunner(Class testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowHttpEntityWrapper.class);
        Robolectric.bindShadowClass(CustomShadowIntent.class);
    }
}
