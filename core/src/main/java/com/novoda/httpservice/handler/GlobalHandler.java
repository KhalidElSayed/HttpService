package com.novoda.httpservice.handler;

import com.novoda.httpservice.provider.IntentWrapper;

public interface GlobalHandler extends RequestHandler {
	
	void onProgress(IntentWrapper intentWrapper);

}
