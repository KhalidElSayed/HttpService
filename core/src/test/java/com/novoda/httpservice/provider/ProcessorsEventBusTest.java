package com.novoda.httpservice.provider;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;

import android.net.Uri;

import com.novoda.httpservice.processor.Processor;

public class ProcessorsEventBusTest {

	private EventBus eventBus;
	
	private IntentWrapper mRequest;
	
	private HttpRequest mHttpRequest;
	
	private HttpResponse mHttpResponse;
	
	private HttpContext mHttpContext;
	
	private Processor mProcessor;
	
	private Response mResponse;
	
	private Uri mUri;
	
	private IntentRegistry mRequestRegistry;
	
	@Before
	public void setUp() {
		mRequestRegistry = mock(IntentRegistry.class);
		eventBus = new EventBus(mRequestRegistry);
		mRequest = mock(IntentWrapper.class);
		mUri = mock(Uri.class);
		when(mRequest.getUri()).thenReturn(mUri);
		mResponse = mock(Response.class);
		when(mResponse.getIntentWrapper()).thenReturn(mRequest);
		mProcessor = mock(Processor.class);
		mHttpContext = mock(HttpContext.class);
		mHttpRequest = mock(HttpRequest.class);
	}
	
	@Test
	public void shouldAddHandlerNotFailIfTheProcessorIsNull() {
		Processor processor = null;
		eventBus.add(processor);
	}
	
	@Test
	public void shouldRemoveHandlerNotFailIfTheProcessorIsNull() {
		Processor processor = null;
		eventBus.remove(processor);
	}
	
	@Test
	public void shouldFireOnPreProcessRequest() throws HttpException, IOException {
		when(mProcessor.match(mRequest)).thenReturn(true);
		eventBus.add(mProcessor);
		eventBus.fireOnPreProcess(mRequest, mHttpRequest, mHttpContext);
		
		verify(mProcessor, times(1)).process(any(HttpRequest.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPreProcessRequestSkipTheProcessorIfDoesntMatch() throws HttpException, IOException {
		when(mProcessor.match(mRequest)).thenReturn(false);
		eventBus.add(mProcessor);
		eventBus.fireOnPreProcess(mRequest, mHttpRequest, mHttpContext);
		
		verify(mProcessor, times(0)).process(any(HttpRequest.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostProcessRequest() throws HttpException, IOException {
		when(mProcessor.match(mRequest)).thenReturn(true);
		eventBus.add(mProcessor);
		eventBus.fireOnPostProcess(mRequest, mHttpResponse, mHttpContext);
		
		verify(mProcessor, times(1)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostProcessRequestSkipTheProcessorIfDoesntMatch() throws HttpException, IOException {
		when(mProcessor.match(mRequest)).thenReturn(false);
		eventBus.add(mProcessor);
		eventBus.fireOnPostProcess(mRequest, mHttpResponse, mHttpContext);
		
		verify(mProcessor, times(0)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostInReverseOrder() throws HttpException, IOException {
		Processor processor1 = mock(Processor.class);
		Processor processor2 = mock(Processor.class);
		
		when(processor1.match(mRequest)).thenThrow(new RuntimeException());
		when(processor2.match(mRequest)).thenReturn(true);
		eventBus.add(processor1);
		eventBus.add(processor2);
		try {
			eventBus.fireOnPostProcess(mRequest, mHttpResponse, mHttpContext);
		} catch(Exception e) {
			//don't care in this case the exception is a trick to check the order
		}
		
		verify(processor2, times(1)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
}
