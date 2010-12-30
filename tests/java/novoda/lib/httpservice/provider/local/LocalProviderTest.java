package novoda.lib.httpservice.provider.local;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.exception.ProviderException;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.http.HttpProvider;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocalProviderTest {
	
	private LocalProvider provider;
	private EventBus eventBus;
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		provider  = new LocalProvider(eventBus);
		provider.add(Uri.parse("http://www.google.com"), "ok");
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfEventBusIsNull() {
		new HttpProvider(null);
	}

	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContentFireOnContentReceived() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(Uri.parse("http://www.google.com"))).thenReturn(true);
		eventBus.add(handler);
		
		Request request = new Request("http://www.google.com");
		
		provider.execute(request);
		
		verify(handler, times(1)).onContentReceived(any(Response.class));
	}
	
	@Test(expected = ProviderException.class)
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(Uri.parse("http://www.foofle.com"))).thenReturn(true);
		eventBus.add(handler);
		
		Request request = new Request("http://www.foofle.com");
		
		provider.execute(request);
		
		verify(handler, times(1)).onThrowable(any(Throwable.class));
	}

}
