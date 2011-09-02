package com.novoda.httpservice.processor.gzip;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.novoda.httpservice.processor.gzip.GZipProcessor.GZipEntity;
import com.novoda.httpservice.util.CustomRobolectricTestRunner;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@Ignore
@RunWith(CustomRobolectricTestRunner.class)
public class GZipProcessorTest {
	
	private HttpContext httpContext = null;

	private GZipProcessor processor;
	
	public GZipProcessorTest() {
	}
	
	@Before
	public void setUp() {
		processor = new GZipProcessor();
	}
	
	@Test
	public void shouldProcessRequest() throws HttpException, IOException {
		HttpRequest httpRequest = mock(HttpRequest.class);
		
		processor.process(httpRequest, httpContext);
		
		verify(httpRequest, times(1)).addHeader("Accept-Encoding", "gzip");
	}
	
	@Test(expected = IOException.class)
	public void shouldProcessResponseThrowExceptionWhenResponseIsNull() throws HttpException, IOException {
		HttpResponse httpResponse = null;
		
		processor.process(httpResponse, httpContext);
		
		verify(httpResponse, times(0)).setEntity(any(GZipEntity.class));
	}
	
	@Test
	public void shouldProcessResponseSkipGZipIfHeaderIsNull() throws HttpException, IOException {
		HttpResponse httpResponse = mock(HttpResponse.class);
		
		processor.process(httpResponse, httpContext);
		
		verify(httpResponse, times(0)).setEntity(any(GZipEntity.class));
	}
	
	@Test
	public void shouldProcessResponseSkipGZipIfDoesNotContainEntity() throws HttpException, IOException {
		HttpResponse httpResponse = mock(HttpResponse.class);
		when(httpResponse.getEntity()).thenReturn(null);
		
		processor.process(httpResponse, httpContext);
		
		verify(httpResponse, times(0)).setEntity(any(GZipEntity.class));
	}
	
	@Test
	public void shouldProcessResponseSkipGZipIfHeaderDoesNotContainHeader() throws HttpException, IOException {
		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		when(httpResponse.getEntity()).thenReturn(httpEntity);
		
		processor.process(httpResponse, httpContext);
		
		verify(httpResponse, times(0)).setEntity(any(GZipEntity.class));
	}
	
	@Test
	public void shouldProcessResponse() throws HttpException, IOException {
		HttpResponse httpResponse = mock(HttpResponse.class);
		HttpEntity httpEntity = mock(HttpEntity.class);
		Header header = mock(Header.class);
		HeaderElement headerElement = mock(HeaderElement.class);
		when(headerElement.getName()).thenReturn("gzip");
		HeaderElement[] headerElements = new HeaderElement[] {
			headerElement	
		};
		when(header.getElements()).thenReturn(headerElements);
		when(httpEntity.getContentEncoding()).thenReturn(header);
		when(httpResponse.getEntity()).thenReturn(httpEntity);
		
		processor.process(httpResponse, httpContext);
		
		verify(httpResponse, times(1)).setEntity(any(GZipEntity.class));
	}

}
