package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GlobalHandlerEventBusTest {

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
		GlobalHandler h = mock(GlobalHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addGlobalHandler(KEY, h);

		eventBus.fireOnContentReceived(request, is);
		
		verify(h).onContentReceived(is);
	}
	
	@Test
	public void shouldFireOnContentReceivedOnMultipleGlobalHandler() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);
		
		eventBus.fireOnContentReceived(request, is);
		
		verify(h2).onContentReceived(is);
		verify(h1).onContentReceived(is);
	}
	
	@Test
	public void shouldNotFireOnContentReceivedIfHasBeenRemoved() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);
		
		eventBus.removeGlobalHandler(KEY, h1);

		eventBus.fireOnContentReceived(request, is);
		
		verify(h2, times(1)).onContentReceived(is);
		verify(h1, times(0)).onContentReceived(is);
	}
	
	@Test
	public void shouldGlobalRemovedWorkInEvenOnSecondCall() {
		GlobalHandler h1 = mock(GlobalHandler.class);
		GlobalHandler h2 = mock(GlobalHandler.class);
		InputStream is = mock(InputStream.class);

		eventBus.addGlobalHandler(KEY, h1);
		eventBus.addGlobalHandler(KEY, h2);

		eventBus.removeGlobalHandler(KEY, h1);
		eventBus.removeGlobalHandler(KEY, h1);
		
		eventBus.fireOnContentReceived(request, is);
		
		verify(h2, times(1)).onContentReceived(is);
		verify(h1, times(0)).onContentReceived(is);
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
