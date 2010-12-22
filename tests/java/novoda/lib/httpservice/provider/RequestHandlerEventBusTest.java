package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestHandlerEventBusTest {

	private static final String KEY = "key";
	
	private EventBus eventBus;
	
	private Request request;
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		request = mock(Request.class);
		when(request.getHandlerKey()).thenReturn(KEY);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnGlobalHandler() {
		RequestHandler h = mock(RequestHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addRequestHandler(KEY, h);

		eventBus.fireOnContentReceived(request, is);
		
		verify(h).onContentReceived(is);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);
		
		eventBus.fireOnContentReceived(request, is);
		
		verify(h2).onContentReceived(is);
		verify(h1).onContentReceived(is);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);
		
		eventBus.removeRequestHandler(KEY, h1);

		eventBus.fireOnContentReceived(request, is);
		
		verify(h2, times(1)).onContentReceived(is);
		verify(h1, times(0)).onContentReceived(is);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);

		eventBus.removeRequestHandler(KEY, h1);
		eventBus.removeRequestHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(request, is);
		
		verify(h2, times(1)).onContentReceived(is);
		verify(h1, times(0)).onContentReceived(is);
	}
	
	@Test
	public void shouldGlobalRemovedNotFailEvenIfHandlerIsNotSet() {
		RequestHandler h1 = mock(RequestHandler.class);
		eventBus.removeRequestHandler(KEY, h1);
	}
	
	@Test
	public void shouldGlobalFireOnThrowable() {
		RequestHandler h = mock(RequestHandler.class);
		Throwable t = mock(Throwable.class);

		eventBus.addRequestHandler(KEY, h);

		eventBus.fireOnThrowable(request, t);
		
		verify(h).onThrowable(t);
	}
	
	@Test
	public void shouldGlobalFireOnThrowableForMultipleHandler() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);
		Throwable t = mock(Throwable.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);
		
		eventBus.removeRequestHandler(KEY, h1);

		eventBus.fireOnThrowable(request, t);
		
		verify(h2, times(1)).onThrowable(t);
		verify(h1, times(0)).onThrowable(t);
	}
	
}
