package com.novoda.lib.httpservice.tester.util;


public class Log {

	private static final String TAG = "httpservice-tester";

	public static final void v(String msg) {
		android.util.Log.v(TAG, msg);
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