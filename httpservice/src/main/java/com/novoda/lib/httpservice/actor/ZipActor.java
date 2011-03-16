package com.novoda.lib.httpservice.actor;

import org.apache.http.HttpResponse;

import android.content.Intent;

import com.novoda.lib.httpservice.storage.Storage;

public abstract class ZipActor extends LoggingActor {

	public ZipActor(Intent intent, Storage storage) {
		super(intent, storage);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onResponseReceived(HttpResponse httpResponse) {
		onResponseReceivedCallback(httpResponse, getIntent(), getStorage());
		super.onResponseReceived(httpResponse);
	}

	@Override
	public boolean onResponseError(int statusCode) {
		onResponseErrorCallback(statusCode, getIntent(), getStorage());
		return super.onResponseError(statusCode);
	}
	
	@Override
	public void onThrowable(Throwable t) {
		onThrowableCallback(t, getIntent(), getStorage());
		super.onThrowable(t);
	}
	
	protected abstract void onThrowableCallback(Throwable t, Intent intent, Storage storage);

	protected abstract void onResponseErrorCallback(int statusCode, Intent intent, Storage storage);
	
	protected abstract void onResponseReceivedCallback(HttpResponse httpResponse, Intent intent, Storage storage);
	
}
