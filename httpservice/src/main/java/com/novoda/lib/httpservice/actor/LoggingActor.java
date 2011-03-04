package com.novoda.lib.httpservice.actor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.Intent;

import com.novoda.lib.httpservice.utils.Log;

public class LoggingActor extends Actor {

	public LoggingActor(Intent intent) {
		super(intent);
	}

	@Override
	protected void onCreate() {
		Log.v("onCreate");
	}

	@Override
	protected void onResume() {
		Log.v("onResume");
	}

	@Override
	protected void onPause() {
		Log.v("onPause");
	}

	@Override
	protected void onStop() {
		Log.v("onStop");
	}

	@Override
	protected void onDestroy() {
		Log.v("onDestroy");
	}

	@Override
	protected void onLowMemory() {
		Log.v("onLowMemory");
	}
	
	
	public void onPreprocess(HttpUriRequest method, HttpContext context) {
		Log.v("onPreprocess");
	}

	public void onPostprocess(HttpResponse httpResponse, HttpContext context) {
		Log.v("onPostprocess");
	}

	public void onThrowable(Throwable t) {
		Log.v("onThrowable");
	}

	public void onResponseReceived(HttpResponse httpResponse) {
		Log.v("onResponseReceived");
	}

}
