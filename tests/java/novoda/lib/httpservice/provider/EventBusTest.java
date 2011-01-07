package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.junit.Before;
import org.junit.Test;

import android.net.Uri;

public abstract class EventBusTest<T extends RequestHandler> {

	private EventBus eventBus;
	
	private Request request;
	
	private Response response;
	
	private Uri uri;
	
	private Class<T> clazz;
	
	public EventBusTest(Class<T> clazz) {
		this.clazz = clazz;
	}
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		request = mock(Request.class);
		uri = mock(Uri.class);
		when(request.getUri()).thenReturn(uri);
		response = mock(Response.class);
		when(response.getRequest()).thenReturn(request);
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

		eventBus.fireOnContentReceived(response);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		T h1 = mock(clazz);
		when(h1.match(request)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(request)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2).onContentReceived(response);
		verify(h1).onContentReceived(response);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		T h1 = mock(clazz);
		when(h1.match(request)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(request)).thenReturn(true);

		eventBus.add(h1);
		eventBus.add(h2);
		
		eventBus.remove(h1);

		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		T h1 = mock(clazz);
		when(h1.match(request)).thenReturn(true);
		
		T h2 = mock(clazz);
		when(h2.match(request)).thenReturn(true);
		
		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.remove(h1);
		eventBus.remove(h1);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
	}
	
	@Test
	public void shouldGlobalRemovedNotFailEvenIfHandlerIsNotSet() {
		T h = mock(clazz);
		eventBus.remove(h);
	}
	
	@Test
	public void shouldGlobalFireOnThrowable() {
		T h = mock(clazz);
		when(h.match(request)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h);

		eventBus.fireOnThrowable(request, t);
		
		verify(h).onThrowable(t);
	}
	
	@Test
	public void shouldFireOnThrowableForMultipleHandler() {
		T h1 = mock(clazz);
		when(h1.match(request)).thenReturn(true);
		T h2 = mock(clazz);
		when(h2.match(request)).thenReturn(true);
		
		Throwable t = mock(Throwable.class);

		eventBus.add(h1);
		eventBus.add(h2);

		eventBus.fireOnThrowable(request, t);
		
		verify(h2, times(1)).onThrowable(t);
		verify(h1, times(1)).onThrowable(t);
	}
	
	@Test
	public void shouldFireOnContentConsumed() {
		T h = mock(clazz);
		when(h.match(request)).thenReturn(true);
		
		eventBus.add(h);
		
		eventBus.fireOnContentConsumed(request);
		
		verify(h, times(1)).onContentConsumed(request);
	}
	
}
