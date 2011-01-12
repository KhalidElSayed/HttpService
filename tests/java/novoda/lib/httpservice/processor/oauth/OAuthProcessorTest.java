package novoda.lib.httpservice.processor.oauth;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import novoda.lib.httpservice.provider.IntentWrapper;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

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
	
	@Test
	public void shouldAlwaysMatch() {
		OAuthProcessor processor = new OAuthProcessor(null, null);
		assertTrue(processor.match(null));
		assertTrue(processor.match(request));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldProcessRequestThrowExceptionIfNull() throws HttpException, IOException {
		HttpRequest httpRequest = null;
		
		OAuthProcessor processor = new OAuthProcessor(null, null);
		processor.process(httpRequest, httpContext);
	}
	
	@Test
	public void shouldProcessNormalHttpRequestButSkipTheSign() throws HttpException, IOException {
		HttpRequest httpRequest = mock(HttpRequest.class);
		
		OAuthProcessor processor = new OAuthProcessor(null, null);
		processor.process(httpRequest, httpContext);
		
		verify(httpRequest, times(0)).setHeader(any(String.class), any(String.class));
	}
	
	@Ignore("Too much to mockup, Should take out the CommonsHttpOAuthConsumer from OAuthProcessor")
	@Test
	public void shouldProcessHttpUriRequest() throws HttpException, IOException, URISyntaxException {
		HttpUriRequest httpRequest = mock(HttpUriRequest.class);
		when(httpRequest.getURI()).thenReturn(new URI("http://www.foofle.com"));
		when(httpRequest.getMethod()).thenReturn("get");
		
		OAuthProcessor processor = new OAuthProcessor("consumerkey", "consumerSecret");
		processor.setTokenWithSecret("token", "secret");
		processor.process(httpRequest, httpContext);
		
		verify(httpRequest, times(1)).setHeader(any(String.class), any(String.class));
	}

}
