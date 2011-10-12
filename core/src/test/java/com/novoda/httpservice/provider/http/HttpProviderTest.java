package com.novoda.httpservice.provider.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import android.net.Uri;

import com.novoda.httpservice.exception.ProviderException;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Provider;
import com.novoda.httpservice.provider.Response;

public class HttpProviderTest {
	
	private static final String URL = "http://www.google.com";
	
	private Provider provider;
	private EventBus eventBus;
	private HttpClient httpClient;
	//private IntentWrapper request = new IntentWrapper(new IntentBuilder("action", URL).build());
	
	private IntentWrapper request;
	
	@Before
	public void setUp() {
		eventBus = mock(EventBus.class);
		httpClient = mock(HttpClient.class);
		request = mock(IntentWrapper.class);
	}
	
	@Test(expected = ProviderException.class)
	public void shouldThrowExceptionIfEventBusIsNull() {
		new HttpProvider(httpClient, null);
	}
	
	@Ignore
	@Test
	public void shouldHttpProviderGoAndFireOnContentReceived() throws ClientProtocolException, IOException {
		HttpResponse response = mock(HttpResponse.class);
		when(httpClient.execute(any(HttpGet.class), any(HttpContext.class))).thenReturn(response);
		provider  = new HttpProvider(httpClient, eventBus);
		
		prepareRequest();
		
		Response actualResponse = provider.execute(request);
		assertNotNull(actualResponse);
		assertEquals("", "");
	}
	
	@Ignore
	@Test(expected = ProviderException.class)
	public void shouldHttpProviderFireOnThrowableIf() throws ClientProtocolException, IOException {
		when(httpClient.execute(any(HttpGet.class))).thenThrow(new RuntimeException());
		provider  = new HttpProvider(httpClient, eventBus);
		prepareRequest();
		
		provider.execute(request);
		
		verify(eventBus, times(1)).fireOnThrowable(any(IntentWrapper.class), any(ProviderException.class));
	}
	
    private void prepareRequest() {
        when(request.getAction()).thenReturn("action");
        Uri uri = mock(Uri.class);
        //when(request.asURI()).thenReturn(asURI);
        when(request.getUri()).thenReturn(uri);
    }
	
}
