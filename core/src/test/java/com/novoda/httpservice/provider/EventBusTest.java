package com.novoda.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;

import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.novoda.httpservice.exception.ProviderException;
import com.novoda.httpservice.handler.RequestHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Bundle.class})
public class EventBusTest {

	private EventBus eventBus;
	
	private IntentWrapper intentWrapper;
	
	private Response mResponse;
	
	private Uri uri;
	
	private IntentRegistry mRequestRegistry;
	
	@Before
	public void setUp() {
		mRequestRegistry = mock(IntentRegistry.class);
		eventBus = new EventBus(mRequestRegistry);
		intentWrapper = mock(IntentWrapper.class);
		uri = mock(Uri.class);
		when(intentWrapper.getUri()).thenReturn(uri);
		mResponse = mock(Response.class);
		when(mResponse.getIntentWrapper()).thenReturn(intentWrapper);
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
	    RequestHandler h = mock(RequestHandler.class);
		
		eventBus.add(h);

		eventBus.fireOnContentReceived(mResponse);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
	    RequestHandler h1 = mock(RequestHandler.class);
		when(h1.match(intentWrapper)).thenReturn(true);
		
		RequestHandler h2 = mock(RequestHandler.class);
		when(h2.match(intentWrapper)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2).onContentReceived(intentWrapper, mResponse);
		verify(h1).onContentReceived(intentWrapper, mResponse);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
	    RequestHandler h1 = mock(RequestHandler.class);
		when(h1.match(intentWrapper)).thenReturn(true);
		
		RequestHandler h2 = mock(RequestHandler.class);
		when(h2.match(intentWrapper)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.remove(h1);

		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2, times(1)).onContentReceived(intentWrapper, mResponse);
		verify(h1, times(0)).onContentReceived(intentWrapper, mResponse);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
	    RequestHandler h1 = mock(RequestHandler.class);
		when(h1.match(intentWrapper)).thenReturn(true);
		
		RequestHandler h2 = mock(RequestHandler.class);
		when(h2.match(intentWrapper)).thenReturn(true);
		
		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.remove(h1);
		eventBus.remove(h1);
		
		eventBus.fireOnContentReceived(mResponse);
		
		verify(h2, times(1)).onContentReceived(intentWrapper, mResponse);
		verify(h1, times(0)).onContentReceived(intentWrapper, mResponse);
	}
	
	@Test
	public void shouldGlobalRemovedNotFailEvenIfHandlerIsNotSet() {
	    RequestHandler h = mock(RequestHandler.class);
		eventBus.remove(h);
	}
	
	@Test
	public void shouldGlobalFireOnThrowable() {
	    RequestHandler h = mock(RequestHandler.class);
		when(h.match(intentWrapper)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h);

		eventBus.fireOnThrowable(intentWrapper, t);
		
		verify(h).onThrowable(intentWrapper, t);
	}
	
	@Test
	public void shouldFireOnThrowableForMultipleHandler() {
	    RequestHandler h1 = mock(RequestHandler.class);
		when(h1.match(intentWrapper)).thenReturn(true);
		RequestHandler h2 = mock(RequestHandler.class);
		when(h2.match(intentWrapper)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.fireOnThrowable(intentWrapper, t);
		
		verify(h2, times(1)).onThrowable(intentWrapper, t);
		verify(h1, times(1)).onThrowable(intentWrapper, t);
	}
	
	@Test
	public void shouldFireOnContentConsumed() {
	    RequestHandler h = mock(RequestHandler.class);
		when(h.match(intentWrapper)).thenReturn(true);
		
		eventBus.add(h);
		
		eventBus.fireOnContentConsumed(intentWrapper);
		
		verify(h, times(1)).onContentConsumed(intentWrapper);
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfRegistryIsNull() {
		eventBus = new EventBus(null);
	}
	
	
	@Test
	public void shouldGetCorrectCodeOnContentReceived() {
	    Response response = mock(Response.class);
	    IntentWrapper iw = mock(IntentWrapper.class);
	    ResultReceiver rr = mock(ResultReceiver.class);
	    when(iw.getResultReceiver()).thenReturn(rr);
	    when(response.getIntentWrapper()).thenReturn(iw);
	    when(response.getStatusCode()).thenReturn(401);
	    
	    eventBus.fireOnContentReceived(response);
	    
	    verify(rr, times(1)).send(401, null);
	}
	
	@Test
    public void shouldReturnErrorCodeInCaseContentIsNull_issue204() {
        Response response = mock(Response.class);
        when(response.getStatusCode()).thenReturn(204);
        when(response.getHttpResponse()).thenReturn(null);
        
        IntentWrapper iw = mock(IntentWrapper.class);
        ResultReceiver rr = mock(ResultReceiver.class);
        
        when(iw.getResultReceiver()).thenReturn(rr);
        when(response.getIntentWrapper()).thenReturn(iw);

        eventBus.fireOnContentReceived(response);
        
        verify(rr, times(1)).send(204, null);
    }
	
}
