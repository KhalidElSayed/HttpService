package com.novoda.lib.httpservice.handler;

import com.novoda.lib.httpservice.provider.IntentWrapper;

public interface GlobalHandler extends RequestHandler {
	
	void onProgress(IntentWrapper intentWrapper);

}
