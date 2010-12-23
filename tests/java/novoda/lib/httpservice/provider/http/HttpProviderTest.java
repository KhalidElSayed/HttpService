package novoda.lib.httpservice.provider.http;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import novoda.lib.httpservice.exception.ProviderException;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpProviderTest {
	
	private static final String URL = "http://www.google.com";
	
	private Provider provider;
	private EventBus eventBus;
	private HttpClient httpClient;
	private Request request = new Request(); {
		request.setUri(Uri.parse(URL));
	}
	
	@Before
	public void setUp() {
		eventBus = mock(EventBus.class);
		httpClient = mock(HttpClient.class);
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfEventBusIsNull() {
		new HttpProvider(httpClient, null);
	}
	
	@Test
	public void shouldHttpProviderGoAndFireOnContentReceived() throws ClientProtocolException, IOException {
		HttpResponse response = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		InputStream is = mock(InputStream.class);
		
		when(httpEntity.getContent()).thenReturn(is);
		when(response.getEntity()).thenReturn(httpEntity);
		when(httpClient.execute(any(HttpGet.class))).thenReturn(response);
		
		provider  = new HttpProvider(httpClient, eventBus);
		
		provider.execute(request);
		
		verify(eventBus, times(1)).fireOnContentReceived(any(Request.class), any(InputStream.class));
	}
	
	@Test(expected = ProviderException.class)
	public void shouldHttpProviderFireOnThrowableIf() throws ClientProtocolException, IOException {
		when(httpClient.execute(any(HttpGet.class))).thenThrow(new RuntimeException());
		provider  = new HttpProvider(httpClient, eventBus);
		
		provider.execute(request);
		
		verify(eventBus, times(1)).fireOnThrowable(any(Request.class), any(ProviderException.class));
	}
	

}
