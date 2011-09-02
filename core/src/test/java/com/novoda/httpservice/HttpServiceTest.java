package com.novoda.httpservice;


import static com.novoda.httpservice.test.Time.await;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentRegistry;
import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.provider.local.LocalProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;

import com.novoda.httpservice.HttpService;
import com.novoda.httpservice.Settings;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(HttpServiceTestRunner.class)
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
