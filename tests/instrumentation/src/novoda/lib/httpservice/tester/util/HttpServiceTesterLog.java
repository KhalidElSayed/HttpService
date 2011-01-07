package novoda.lib.httpservice.tester.util;

import android.util.Log;

public class HttpServiceTesterLog {
	
	public static class Default {
		
		private static final String TAG = "HttpService-Test";
		
		public static final void d(String msg) {
			Log.d(TAG, msg);
		}
		
		public static final void w(String msg) {
			Log.w(TAG, msg);
		}
		
		public static final void e(String msg) {
			Log.e(TAG, msg);
		}
		
		public static final void e(String msg, Throwable t) {
			Log.e(TAG, msg, t);
		}
	}
	
}