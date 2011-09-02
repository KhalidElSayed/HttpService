package com.novoda.httpservice.service.executor;

import static com.novoda.httpservice.util.Log.v;
import static com.novoda.httpservice.util.Log.verboseLoggingEnabled;

import java.util.concurrent.Callable;

import com.novoda.httpservice.exception.HandlerException;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Provider;
import com.novoda.httpservice.provider.Response;

public class CallableWrapper implements Callable<Response> {
	
	private Provider provider;
	
	private IntentWrapper request;
	
	public CallableWrapper(Provider provider, IntentWrapper request) {
		if(provider == null) {
			throw new HandlerException("Configuration problem! A Provider must be specified!");
		}
		this.provider = provider;
		if(request == null) {
			throw new HandlerException("Configuration problem! Request must be specified!");
		}
		this.request = request;
	}
	
	@Override
	public Response call() throws Exception {
		if(verboseLoggingEnabled()) {
			v("Executing request : " + request);
		}
		Response response = provider.execute(request);
		if(verboseLoggingEnabled()) {
			v("Response received");
		}
		return response;
	}

}
