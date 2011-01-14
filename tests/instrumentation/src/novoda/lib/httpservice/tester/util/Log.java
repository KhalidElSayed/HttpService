package novoda.lib.httpservice.tester.util;


public class Log {

	private static final String TAG = "HttpService-Tester";

	public static final void d(String msg) {
		android.util.Log.d(TAG, msg);
	}

	public static final void w(String msg) {
		android.util.Log.w(TAG, msg);
	}

	public static final void e(String msg) {
		android.util.Log.e(TAG, msg);
	}

	public static final void e(String msg, Throwable t) {
		android.util.Log.e(TAG, msg, t);
	}

}