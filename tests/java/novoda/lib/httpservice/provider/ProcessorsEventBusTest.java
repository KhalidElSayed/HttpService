package novoda.lib.httpservice.provider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;
import android.net.Uri;

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
	
	@Ignore("Need to do the implementation")
	@Test
	public void shouldFireOnPreProcessRequest() {
	}
	
	@Ignore("Need to do the implementation")
	@Test
	public void shouldFireOnPostProcessRequest() {
	}
	
	@Ignore("Need to do the implementation")
	@Test
	public void shouldFireOnPostInReverseOrder() {
	}
	
}
