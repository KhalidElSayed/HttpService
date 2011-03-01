package com.novoda.lib.httpservice.tester.service;

import static com.novoda.lib.httpservice.tester.util.Log.v;

import com.novoda.lib.httpservice.SimpleRequestHandler;
import com.novoda.lib.httpservice.provider.IntentWrapper;
import com.novoda.lib.httpservice.provider.Response;

public class CustomRequestHandler extends SimpleRequestHandler {
	
	private static final String PATH = "/";
	
	@Override
	public boolean match(IntentWrapper intentWrapper) {
		if(PATH.equals(intentWrapper.getUri().getPath())) {
			v("do match!");
			return true;
		}
		v("doesn't match!");
		return false;
	}
	
	@Override
	public void onContentReceived(IntentWrapper intentWrapper, Response response) {
		v("Received content for request handler : " + response.getContentAsString().length());
	}

}
