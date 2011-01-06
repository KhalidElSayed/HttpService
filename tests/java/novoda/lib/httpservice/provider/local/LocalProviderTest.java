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
import novoda.lib.httpservice.request.IntentRequestBuilder;
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
	
	private static final String URL = "http://www.google.com";
	private static final String FOOFLE_URL = "http://www.foofle.com";
	private Request request = new Request(new IntentRequestBuilder("action", URL).build());
	private Request foofleRequest = new Request(new IntentRequestBuilder("action", FOOFLE_URL).build());
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		provider  = new LocalProvider(eventBus);
		provider.add(Uri.parse(URL), "ok");
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfEventBusIsNull() {
		new HttpProvider(null);
	}

	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContentFireOnContentReceived() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(request)).thenReturn(true);
		eventBus.add(handler);
		
		provider.execute(request);
		
		verify(handler, times(1)).onContentReceived(any(Response.class));
	}
	
	@Test(expected = ProviderException.class)
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(foofleRequest)).thenReturn(true);
		eventBus.add(handler);
		
		Request request = new Request(new IntentRequestBuilder("action", FOOFLE_URL).build());
		
		provider.execute(request);
		
		verify(handler, times(1)).onThrowable(any(Throwable.class));
	}

}
