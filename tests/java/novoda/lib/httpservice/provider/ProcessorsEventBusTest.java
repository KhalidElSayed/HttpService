package novoda.lib.httpservice.provider;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ProcessorsEventBusTest {

	private EventBus eventBus;
	
	private Request request;
	
	private HttpRequest httpRequest;
	
	private HttpResponse httpResponse;
	
	private HttpContext httpContext;
	
	private Processor processor;
	
	private Response response;
	
	private Uri uri;
	
	@Before
	public void setUp() {
		eventBus = new EventBus();
		request = mock(Request.class);
		uri = mock(Uri.class);
		when(request.getUri()).thenReturn(uri);
		response = mock(Response.class);
		when(response.getRequest()).thenReturn(request);
		processor = mock(Processor.class);
		httpContext = mock(HttpContext.class);
		httpRequest = mock(HttpRequest.class);
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
		when(processor.match(uri)).thenReturn(true);
		eventBus.add(processor);
		eventBus.fireOnPreProcessRequest(uri, httpRequest, httpContext);
		
		verify(processor, times(1)).process(any(HttpRequest.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPreProcessRequestSkipTheProcessorIfDoesntMatch() throws HttpException, IOException {
		when(processor.match(uri)).thenReturn(false);
		eventBus.add(processor);
		eventBus.fireOnPreProcessRequest(uri, httpRequest, httpContext);
		
		verify(processor, times(0)).process(any(HttpRequest.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostProcessRequest() throws HttpException, IOException {
		when(processor.match(uri)).thenReturn(true);
		eventBus.add(processor);
		eventBus.fireOnPostProcessRequest(uri, httpResponse, httpContext);
		
		verify(processor, times(1)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostProcessRequestSkipTheProcessorIfDoesntMatch() throws HttpException, IOException {
		when(processor.match(uri)).thenReturn(false);
		eventBus.add(processor);
		eventBus.fireOnPostProcessRequest(uri, httpResponse, httpContext);
		
		verify(processor, times(0)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
	@Test
	public void shouldFireOnPostInReverseOrder() throws HttpException, IOException {
		Processor processor1 = mock(Processor.class);
		Processor processor2 = mock(Processor.class);
		
		when(processor1.match(uri)).thenThrow(new RuntimeException());
		when(processor2.match(uri)).thenReturn(true);
		eventBus.add(processor1);
		eventBus.add(processor2);
		try {
			eventBus.fireOnPostProcessRequest(uri, httpResponse, httpContext);
		} catch(Exception e) {
			//don't care in this case the exception is a trick to check the order
		}
		
		verify(processor2, times(1)).process(any(HttpResponse.class), any(HttpContext.class));
	}
	
}
