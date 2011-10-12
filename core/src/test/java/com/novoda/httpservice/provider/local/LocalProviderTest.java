package com.novoda.httpservice.provider.local;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.novoda.httpservice.Settings;
import com.novoda.httpservice.exception.ProviderException;
import com.novoda.httpservice.handler.GlobalHandler;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentRegistry;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.provider.http.HttpProvider;
import com.novoda.httpservice.util.IntentBuilder;
import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
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
