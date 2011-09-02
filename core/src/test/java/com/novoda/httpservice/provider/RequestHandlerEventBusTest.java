package com.novoda.httpservice.provider;

import com.novoda.httpservice.handler.RequestHandler;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class RequestHandlerEventBusTest extends EventBusTest<RequestHandler>{

	public RequestHandlerEventBusTest() {
		super(RequestHandler.class);
	}
	
}
