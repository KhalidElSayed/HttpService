
package novoda.lib.httpservice.tester;

import static novoda.lib.httpservice.tester.util.HttpServiceTesterLog.Default.d;
import android.app.Application;

/**
 * @author luigi.agosti
 */
public class HttpServiceTester extends Application {

    private static HttpServiceTester instance;
    
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        d("============================================");
        d("Create event : Start up");
        instance = this;
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        d("Low memory waring.");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        d("On terminate : Shutting down");
        d("============================================");
    }

    public static HttpServiceTester getInstance() {
        return instance;
    }

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
