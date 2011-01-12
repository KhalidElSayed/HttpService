package novoda.lib.httpservice.util;

public class Log {
	
	//Default
	private static final String TAG = "HttpService";
	
	public static final boolean verboseLoggingEnabled() {
		return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
	}
	
	public static final boolean infoLoggingEnabled() {
		return android.util.Log.isLoggable(TAG, android.util.Log.INFO);
	}
	
	public static final boolean errorLoggingEnabled() {
		return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
	}
	
	public static final void e(String msg) {
		android.util.Log.e(TAG, msg);
	}
	
	public static final void e(String msg, Throwable t) {
		android.util.Log.e(TAG, msg, t);
	}
	
	public static final void w(String msg, Throwable t) {
		android.util.Log.w(TAG, msg, t);
	}
	
	public static final void v(String msg) {
		android.util.Log.d(TAG, msg);
	}
	
	public static final void i(String msg) {
		android.util.Log.i(TAG, msg);
	}
	
	public static class Provider {
		
		private static final String TAG = "HttpService-Provider";
		
		public static final boolean verboseLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
		}
		
		public static final boolean errorLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
		}
		
		public static final boolean warnLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.WARN);
		}
		
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
	
	public static class Processor {

		private static final String TAG = "HttpService-Processor";
		
		public static final boolean verboseLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
		}
		
		public static final boolean infoLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.INFO);
		}
		
		public static final boolean errorLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
		}
		
		public static final void e(String msg) {
			android.util.Log.e(TAG, msg);
		}
		
		public static final void e(String msg, Throwable t) {
			android.util.Log.e(TAG, msg, t);
		}
		
		public static final void w(String msg, Throwable t) {
			android.util.Log.w(TAG, msg, t);
		}
		
		public static final void v(String msg) {
			android.util.Log.d(TAG, msg);
		}
		
		public static final void i(String msg) {
			android.util.Log.i(TAG, msg);
		}
	}
	
	public static class Bus {
		
		private static final String TAG = "HttpService-Bus";
				
		public static final boolean verboseLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
		}
		
		public static final boolean infoLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.INFO);
		}
		
		public static final boolean errorLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.ERROR);
		}
		
		public static final boolean warnLoggingEnabled() {
			return android.util.Log.isLoggable(TAG, android.util.Log.WARN);
		}
		
		public static final void e(String msg) {
			android.util.Log.e(TAG, msg);
		}
		
		public static final void e(String msg, Throwable t) {
			android.util.Log.e(TAG, msg, t);
		}
		
		public static final void w(String msg) {
			android.util.Log.w(TAG, msg);
		}
		
		public static final void v(String msg) {
			android.util.Log.d(TAG, msg);
		}
		
		public static final void i(String msg) {
			android.util.Log.i(TAG, msg);
		}
	}
	
}