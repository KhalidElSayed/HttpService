package novoda.lib.httpservice.provider.local;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.Settings;
import novoda.lib.httpservice.exception.ProviderException;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.IntentRegistry;
import novoda.lib.httpservice.provider.IntentWrapper;
import novoda.lib.httpservice.provider.Response;
import novoda.lib.httpservice.provider.http.HttpProvider;
import novoda.lib.httpservice.util.IntentBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocalProviderTest {
	
	private LocalProvider provider;
	private EventBus eventBus;
	private IntentRegistry mIntentRegistry;
	
	private static final String URL = "http://www.google.com";
	private static final String FOOFLE_URL = "http://www.foofle.com";
	private IntentWrapper intentWrapper = new IntentWrapper(new IntentBuilder("action", URL).build());
	private IntentWrapper foofleIntentWrapper = new IntentWrapper(new IntentBuilder("action", FOOFLE_URL).build());
	
	@Before
	public void setUp() {
		mIntentRegistry = mock(IntentRegistry.class);
		eventBus = new EventBus(mIntentRegistry);
		provider  = new LocalProvider(eventBus);
		provider.add(Uri.parse(URL), "ok");
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfEventBusIsNull() {
		new HttpProvider(null, new Settings());
	}

	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContentFireOnContentReceived() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(intentWrapper)).thenReturn(true);
		eventBus.add(handler);
		
		provider.execute(intentWrapper);
		
		verify(handler, times(1)).onContentReceived(any(IntentWrapper.class), any(Response.class));
	}
	
	@Test(expected = ProviderException.class)
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		GlobalHandler handler = mock(GlobalHandler.class);
		when(handler.match(foofleIntentWrapper)).thenReturn(true);
		eventBus.add(handler);
		
		IntentWrapper intentWrapper = new IntentWrapper(new IntentBuilder("action", FOOFLE_URL).build());
		
		provider.execute(intentWrapper);
		
		verify(handler, times(1)).onThrowable(any(IntentWrapper.class), any(Throwable.class));
	}

}
