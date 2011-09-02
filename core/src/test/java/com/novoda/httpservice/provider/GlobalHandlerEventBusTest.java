package com.novoda.httpservice.provider;

import com.novoda.httpservice.handler.GlobalHandler;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class GlobalHandlerEventBusTest extends EventBusTest<GlobalHandler>{

	public GlobalHandlerEventBusTest() {
		super(GlobalHandler.class);
	}
	
}
