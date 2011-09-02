package com.novoda.httpservice.tester;

import static com.novoda.httpservice.tester.util.Log.d;
import android.app.Application;

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
