package novoda.lib.httpservice.util;

import android.util.Log;

public class LogTag {
	
	private static final String EXECUTOR_SERVICE = "ExecutorService";
	
	private static final String NET_SERVICE = "NetService";
	
	public static final boolean debugIsEnableForES() {
		return Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.DEBUG);
	}
	
	public static final boolean infoIsEnableForES() {
		return Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.INFO);
	}
	
	public static final void debugES(String msg) {
		Log.d(EXECUTOR_SERVICE, msg);
	}

	public static final void infoES(String msg) {
		Log.i(EXECUTOR_SERVICE, msg);
	}
	
	public static final boolean debugIsEnableForNS() {
		return Log.isLoggable(LogTag.NET_SERVICE, Log.DEBUG);
	}
	
	public static final boolean infoIsEnableForNS() {
		return Log.isLoggable(LogTag.NET_SERVICE, Log.DEBUG);
	}
	
	public static final void debugNS(String msg) {
		Log.d(NET_SERVICE, msg);
	}

	public static final void infoNS(String msg) {
		Log.i(NET_SERVICE, msg);
	}


}