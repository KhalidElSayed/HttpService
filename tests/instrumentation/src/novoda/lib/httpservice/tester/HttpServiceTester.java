
package novoda.lib.httpservice.tester;

import novoda.lib.httpservice.tester.util.AppLogger;
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
        if (AppLogger.isInfoEnabled()) {
            AppLogger.info("============================================");
            AppLogger.info("Create event : Start up");
        }
        instance = this;
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (AppLogger.isInfoEnabled()) {
            AppLogger.info("Low memory waring.");
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (AppLogger.isInfoEnabled()) {
            AppLogger.info("On terminate : Shutting down");
            AppLogger.info("============================================");
        }
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
