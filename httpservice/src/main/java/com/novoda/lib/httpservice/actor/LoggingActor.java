package com.novoda.lib.httpservice.actor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import android.content.Intent;

import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.utils.Log;

public class LoggingActor extends Actor {

	public LoggingActor(Intent intent, Storage storage) {
		super(intent, storage);
	}

	@Override
	public void onCreate() {
		Log.v("onCreate");
	}

	@Override
	public void onResume() {
		Log.v("onResume");
	}

	@Override
	public void onPause() {
		Log.v("onPause");
	}

	@Override
	public void onStop() {
		Log.v("onStop");
	}

	@Override
	public void onDestroy() {
		Log.v("onDestroy");
	}

	@Override
	public void onLowMemory() {
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
