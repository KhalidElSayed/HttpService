package com.novoda.httpservice.service.executor;

import static org.mockito.Mockito.mock;

import com.novoda.httpservice.exception.HandlerException;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.local.LocalProvider;
import com.novoda.httpservice.util.IntentBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class CallableWrapperTest {
	
	private EventBus mEventBus;
	
	@Before
	public void setUp() {
		mEventBus = mock(EventBus.class);
	}

	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfRequestIsNull() throws Exception {
		new CallableWrapper(new LocalProvider(mEventBus), null).call();
	}
	
	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfLocalProviderIsNull() throws Exception {
		new CallableWrapper(null, new IntentWrapper(new IntentBuilder("test","http://www.google.com").build())).call();
	}
	
}
