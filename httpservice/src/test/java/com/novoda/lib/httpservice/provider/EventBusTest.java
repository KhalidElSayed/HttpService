package com.novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.novoda.lib.httpservice.exception.ProviderException;
import com.novoda.lib.httpservice.handler.RequestHandler;

import org.junit.Before;
import org.junit.Test;

import android.net.Uri;

public abstract class EventBusTest<T extends RequestHandler> {

	private EventBus eventBus;
	
	private IntentWrapper mIntentWrapper;
	
	private Response mResponse;
	
	private Uri mUri;
	
	private Class<T> clazz;
	
	private IntentRegistry mRequestRegistry;
	
	public EventBusTest(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Before
	public void setUp() {
		mRequestRegistry = mock(IntentRegistry.class);
		eventBus = new EventBus(mRequestRegistry);
		mIntentWrapper = mock(IntentWrapper.class);
		mUri = mock(Uri.class);
		when(mIntentWrapper.getUri()).thenReturn(mUri);
		mResponse = mock(Response.class);
		when(mResponse.getIntentWrapper()).thenReturn(mIntentWrapper);
	}
	
	@Test
	public void shouldAddHandlerNotFailIfTheHandlerIsNull() {
		RequestHandler handler = null;
		eventBus.add(handler);
	}
	
	@Test
	public void shouldRemoveHandlerNotFailIfTheHandlerIsNull() {
		RequestHandler handler = null;
		eventBus.remove(handler);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnGlobalHandler() {
		T h = mock(clazz);
		
		eventBus.add(h);

		eventBus.fireOnContentReceived(mResponse);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		T h1 = mock(clazz);
		when(h1.match(mIntentWrapper)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(mIntentWrapper)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2).onContentReceived(mIntentWrapper, mResponse);
		verify(h1).onContentReceived(mIntentWrapper, mResponse);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		T h1 = mock(clazz);
		when(h1.match(mIntentWrapper)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(mIntentWrapper)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.remove(h1);

		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2, times(1)).onContentReceived(mIntentWrapper, mResponse);
		verify(h1, times(0)).onContentReceived(mIntentWrapper, mResponse);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		T h1 = mock(clazz);
		when(h1.match(mIntentWrapper)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(mIntentWrapper)).thenReturn(true);
		
		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.remove(h1);
		eventBus.remove(h1);
		
		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2, times(1)).onContentReceived(mIntentWrapper, mResponse);
		verify(h1, times(0)).onContentReceived(mIntentWrapper, mResponse);
	}
	
	@Test
	public void shouldGlobalRemovedNotFailEvenIfHandlerIsNotSet() {
		T h = mock(clazz);
		eventBus.remove(h);
	}
	
	@Test
	public void shouldGlobalFireOnThrowable() {
		T h = mock(clazz);
		when(h.match(mIntentWrapper)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h);

		eventBus.fireOnThrowable(mIntentWrapper, t);
		
		verify(h).onThrowable(mIntentWrapper, t);
	}
	
	@Test
	public void shouldFireOnThrowableForMultipleHandler() {
		T h1 = mock(clazz);
		when(h1.match(mIntentWrapper)).thenReturn(true);
		T h2 = mock(clazz);
		when(h2.match(mIntentWrapper)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.fireOnThrowable(mIntentWrapper, t);
		
		verify(h2, times(1)).onThrowable(mIntentWrapper, t);
		verify(h1, times(1)).onThrowable(mIntentWrapper, t);
	}
	
	@Test
	public void shouldFireOnContentConsumed() {
		T h = mock(clazz);
		when(h.match(mIntentWrapper)).thenReturn(true);
		
		eventBus.add(h);
		
		eventBus.fireOnContentConsumed(mIntentWrapper);
		
		verify(h, times(1)).onContentConsumed(mIntentWrapper);
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfRegistryIsNull() {
		eventBus = new EventBus(null);
	}
	
}
