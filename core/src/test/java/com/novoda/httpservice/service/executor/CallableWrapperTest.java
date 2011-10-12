package com.novoda.httpservice.service.executor;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import android.content.Intent;

import com.novoda.httpservice.exception.HandlerException;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.local.LocalProvider;

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
	    Intent i = Mockito.mock(Intent.class);
		new CallableWrapper(null, new IntentWrapper(i)).call();
	}
	
}
