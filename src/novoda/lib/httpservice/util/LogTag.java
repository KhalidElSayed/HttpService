package novoda.lib.httpservice.util;

import android.util.Log;

public class LogTag {
	
	public static class Provider {
		
		private static final String TAG = "HttpService-Provider";
		
		public static final boolean debugIsEnable() {
			return Log.isLoggable(TAG, Log.DEBUG);
		}
		
		public static final boolean errorIsEnable() {
			return Log.isLoggable(TAG, Log.ERROR);
		}
		
		public static final boolean warnIsEnable() {
			return Log.isLoggable(TAG, Log.WARN);
		}
		
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
	
	public static class Processor {
		
		private static final String TAG = "HttpService-Processor";
		
		public static final boolean debugIsEnable() {
			return Log.isLoggable(TAG, Log.DEBUG);
		}
		
		public static final boolean errorIsEnable() {
			return Log.isLoggable(TAG, Log.ERROR);
		}
		
		public static final boolean warnIsEnable() {
			return Log.isLoggable(TAG, Log.WARN);
		}
		
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
			t.printStackTrace();
		}
	}
	
	public static class Core {
		
		private static final String TAG = "HttpService-Core";
				
		public static final boolean debugIsEnable() {
			return Log.isLoggable(TAG, Log.DEBUG);
		}
		
		public static final boolean infoIsEnable() {
			return Log.isLoggable(TAG, Log.DEBUG);
		}
		
		public static final boolean errorIsEnable() {
			return Log.isLoggable(TAG, Log.ERROR);
		}
		
		public static final void e(String msg) {
			Log.e(TAG, msg);
		}
		
		public static final void e(String msg, Throwable t) {
			Log.e(TAG, msg, t);
		}
		
		public static final void w(String msg, Throwable t) {
			Log.w(TAG, msg, t);
		}
		
		public static final void d(String msg) {
			Log.d(TAG, msg);
		}
		
		public static final void i(String msg) {
			Log.i(TAG, msg);
		}
	}
	
}