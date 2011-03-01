package com.novoda.lib.httpservice;

import com.novoda.lib.httpservice.handler.GlobalHandler;
import com.novoda.lib.httpservice.provider.IntentWrapper;

/**
 * Empty implementation of GlobalHandler always matching.
 * Nice to extends so that you can override only the necessary methods
 * 
 * @author luigi@novoda.com
 *
 */
public class SimpleGlobalHandler extends SimpleRequestHandler implements GlobalHandler {

	@Override
	public void onProgress(IntentWrapper intentWrapper) {
	}

}
