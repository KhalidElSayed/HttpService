
package novoda.lib.httpservice.auth;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

@SuppressWarnings("unchecked")
public class OAuthAccountTest extends ActivityInstrumentationTestCase2 {
    private static final String TARGET_PACKAGE_ID = "com.android.development";

    private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.android.development.AccountsTester";

    private static Class<?> launcherActivityClass;
    static {
        try {
            launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public OAuthAccountTest() {
        super(TARGET_PACKAGE_ID, launcherActivityClass);
    }

    private Solo solo;

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    public void testCanOpenSettings() {
        solo.pressMenuItem(0);
    }

    @Override
    public void tearDown() throws Exception {
        try {
            solo.finalize();
        } catch (Throwable e) {

            e.printStackTrace();
        }
        getActivity().finish();
        super.tearDown();
    }
}
