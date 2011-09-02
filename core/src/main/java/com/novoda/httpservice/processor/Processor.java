package com.novoda.httpservice.processor;

import com.novoda.httpservice.provider.IntentWrapper;

import org.apache.http.protocol.HttpProcessor;

public interface Processor extends HttpProcessor {

	boolean match(IntentWrapper request);
	
}
