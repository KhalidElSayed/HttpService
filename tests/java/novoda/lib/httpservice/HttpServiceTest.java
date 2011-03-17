package novoda.lib.httpservice;


import static novoda.lib.httpservice.test.Time.await;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.IntentRegistry;
import novoda.lib.httpservice.provider.Response;
import novoda.lib.httpservice.provider.local.LocalProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpServiceTest {
	
	private static final String URL = "http://www.google.com";
	
	private EventBus eventBus = mock(EventBus.class);

	private LocalProvider provider = new LocalProvider(eventBus); {
		provider.add(URL, "ok");
	}

	private Intent mIntent;
	
	private EventBus mEventBus;
	
	private IntentRegistry mRequestRegistry;
	
	@Before
	public void setUp() {
		mIntent = mock(Intent.class);
		when(mIntent.getData()).thenReturn(Uri.parse(URL));
		mEventBus = mock(EventBus.class);
		mRequestRegistry = mock(IntentRegistry.class);
	}

	@Ignore
	@Test
	public void shouldFireEventOnTheBus() {
		HttpService service = new HttpService(provider, mRequestRegistry, mEventBus, null, new Settings()) { };
		service.startService(mIntent);
		await(200);
		verify(mEventBus, times(1)).fireOnContentReceived(any(Response.class));
	}
	
}
