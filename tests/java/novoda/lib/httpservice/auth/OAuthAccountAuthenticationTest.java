package novoda.lib.httpservice.auth;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(RobolectricTestRunner.class)
public class OAuthAccountAuthenticationTest {

    private OAuthAccountAuthenticatorService service;

    @Before
    public void setUp() throws Exception {
        service = new OAuthAccountAuthenticatorService();
    }

    @Test(expected=IllegalStateException.class)
    public void testShouldThrowAnExceptionifMetadataNotPresent() {
        service.onCreate();
    }

}
