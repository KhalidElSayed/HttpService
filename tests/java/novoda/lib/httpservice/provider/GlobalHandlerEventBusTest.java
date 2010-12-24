package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GlobalHandlerEventBusTest {

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
		GlobalHandler h = mock(GlobalHandler.class);
		
		eventBus.addGlobalHandler(KEY, h);

		eventBus.fireOnContentReceived(response);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2).onContentReceived(response);
		verify(h1).onContentReceived(response);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);
		
		eventBus.removeGlobalHandler(KEY, h1);

		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);

		eventBus.removeGlobalHandler(KEY, h1);
		eventBus.removeGlobalHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(response);
		
		verify(h2, times(1)).onContentReceived(response);
		verify(h1, times(0)).onContentReceived(response);
	}
	
	@Test
	public void shouldGlobalRemovedNotFailEvenIfHandlerIsNotSet() {
		GlobalHandler h = mock(GlobalHandler.class);
		eventBus.removeGlobalHandler(KEY, h);
	}
	
	@Test
	public void shouldGlobalFireOnThrowable() {
		GlobalHandler h = mock(GlobalHandler.class);
		Throwable t = mock(Throwable.class);

		eventBus.addGlobalHandler(KEY, h);

		eventBus.fireOnThrowable(request, t);
		
		verify(h).onThrowable(t);
	}
	
	@Test
	public void shouldGlobalFireOnThrowableForMultipleHandler() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);
		Throwable t = mock(Throwable.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);
		
		eventBus.removeGlobalHandler(KEY, h1);

		eventBus.fireOnThrowable(request, t);
		
		verify(h2, times(1)).onThrowable(t);
		verify(h1, times(0)).onThrowable(t);
	}
	
	@Test
	public void shouldUseDefaultKeyOnAdd() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		Throwable t = mock(Throwable.class);
		request = mock(Request.class);
		when(request.getHandlerKey()).thenReturn(null);
		
		eventBus.addGlobalHandler(h1);
		
		eventBus.fireOnThrowable(request, t);
		
		verify(h1, times(1)).onThrowable(t);
	}
	
}
