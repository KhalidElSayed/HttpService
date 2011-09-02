package com.novoda.httpservice.processor.oauth;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.novoda.httpservice.provider.IntentWrapper;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class OAuthProcessorTest {
	
	private HttpContext httpContext = null;
	private IntentWrapper request;
	private Uri uri = Uri.parse("http://www.foofle.com");
	
	@Before
	public void setup() {
		request = mock(IntentWrapper.class);
		when(request.getUri()).thenReturn(uri);
	}
	
	@Ignore
	@Test
	public void shouldAlwaysMatch() {
		Context context = null;
		OAuthProcessor processor = new OAuthProcessor(context, null);
		assertTrue(processor.match(null));
		assertTrue(processor.match(request));
	}
	
	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void shouldProcessRequestThrowExceptionIfNull() throws HttpException, IOException {
		HttpRequest httpRequest = null;
		Context context = null;
		OAuthProcessor processor = new OAuthProcessor(context, null);
		processor.process(httpRequest, httpContext);
	}
	
	@Ignore
	@Test
	public void shouldProcessNormalHttpRequestButSkipTheSign() throws HttpException, IOException {
		HttpRequest httpRequest = mock(HttpRequest.class);
		Context context = null;
		OAuthProcessor processor = new OAuthProcessor(context, null);
		processor.process(httpRequest, httpContext);
		
		verify(httpRequest, times(0)).setHeader(any(String.class), any(String.class));
	}

}
