package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestHandlerEventBusTest {

	private static final String KEY = "key";
	
	private EventBus eventBus;
	
	private Request request;
	
	private Response response;
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		request = mock(Request.class);
		when(request.getHandlerKey()).thenReturn(KEY);
		response = mock(Response.class);
		when(response.getRequest()).thenReturn(request);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnGlobalHandler() {
		RequestHandler h = mock(RequestHandler.class);

		eventBus.addRequestHandler(KEY, h);

		eventBus.fireOnContentReceived(response);
		
		verify(h).onContentReceived(response);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);
		
		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2).onContentReceived(response);
		verify(h1).onContentReceived(response);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);
		
		eventBus.removeRequestHandler(KEY, h1);

		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		RequestHandler h1 = mock(RequestHandler.class);
		RequestHandler h2 = mock(RequestHandler.class);

		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h2);

		eventBus.removeRequestHandler(KEY, h1);
		eventBus.removeRequestHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
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
	
	@Test
	public void shouldNotHaveMultipleHandlerForTheSameRequestHandler() {
		RequestHandler h1 = mock(RequestHandler.class);
		
		eventBus.addRequestHandler(KEY, h1);
		eventBus.addRequestHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h1, times(1)).onContentReceived(response);
	}
	
	@Test
	public void shouldNotHaveMultipleHandlerForTheSameGlobalHandler() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		
		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h1, times(1)).onContentReceived(response);
	}
	
}
