package novoda.lib.httpservice.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;

import novoda.lib.httpservice.exception.RequestException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestTest {

	private static final String ACTION = "ACTION";
	
	private Intent intent;
	
	@Test
	public void shouldIsGetRespondTrueIfSettedAccondingly() {
		Intent intent = new IntentRequestBuilder(ACTION, "http://www.google.com").build();
		
		Request r = new Request(intent);

		assertTrue(r.isGet());
		assertFalse(r.isPost());
	}
	
	@Test
	public void shouldIsPostRespondTrueIfSettedAccondingly() {
		Intent intent = new IntentRequestBuilder("", "").asPost().build();
		
		Request r = new Request(intent);

		assertTrue(r.isPost());
		assertFalse(r.isGet());
	}
	
	@Ignore //I think is related to robolectric
	@Test
	public void shouldGenerateURIfromUri() {
		Uri uri = mock(Uri.class);
		when(uri.getScheme()).thenReturn("http");
		when(uri.getHost()).thenReturn("www.google.com");
		when(uri.getPort()).thenReturn(80);
		when(uri.getEncodedPath()).thenReturn("/relative/path");
		when(uri.getFragment()).thenReturn("");
		
		URI result = Request.asURI(uri);
		
		assertNotNull(result);
		assertEquals("http", result.getScheme());
		assertEquals("www.google.com", result.getHost());
		assertEquals(80, result.getPort());
		assertEquals("/relative/path", uri.getEncodedPath());
		assertEquals("", uri.getQuery());
		assertEquals("", uri.getFragment());
	}
	
	@Before
	public void setUpValidDefaultRequest() {
		intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(ACTION);
		
		Uri uri = mock(Uri.class);
		when(intent.getData()).thenReturn(uri);
	}
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfIntentIsNull() {
		new Request(null);
	}
	
//	@Test(expected = RequestException.class)
//	public void shouldThrowRequestExceptionIfTheActionIsNotImplemented() {
//		Intent intent = mock(Intent.class);
//		when(intent.getAction()).thenReturn("action.not.implemented");
//		
//		IntentRequestParser.parse(intent);
//	}
	
	@Test
	public void shouldBuildSimpleHttpRequestDoesNotFailIfUrlAndUriAreNotProvided() {
		Intent intent = mock(Intent.class);
		
		new Request(intent);
	}
	
	@Test
	public void shoudBuildRequestFromIntentWithOnlyUri() {
		Uri uri = mock(Uri.class);
		when(intent.getData()).thenReturn(uri);

		Request request = new Request(intent);
		
		assertNotNull(request);
		assertEquals(uri, request.getUri());
	}
	
	@Test
	public void shouldSetTheRequestReceiverIfThere() {
		ResultReceiver expectedReceived = mock(ResultReceiver.class);
		when(intent.getParcelableExtra(Request.Extra.result_receiver)).thenReturn(expectedReceived);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		ResultReceiver actualReceived = request.getResultReceiver();
		assertNotNull(actualReceived);
		assertEquals(expectedReceived, actualReceived);
	}
	
	@Test
	public void shouldBuildRequestWithGetMethodByDefault() {
		when(intent.getIntExtra(Request.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.GET);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertTrue(request.isGet());
	}
	
	@Test
	public void shouldBuildWithPostMethod() {
		when(intent.getIntExtra(Request.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.POST);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertTrue(request.isPost());
	}
	
	@Test
	public void shouldReadTheHandlerKey() {
		when(intent.getStringExtra(Request.Extra.handler_key)).thenReturn("specificHandler");
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertEquals("specificHandler", request.getHandlerKey());
	}
	
	@Test
	public void shouldReadParams() {
		ArrayList<Parcelable> list = new ArrayList<Parcelable>();
		when(intent.getParcelableArrayListExtra(Request.Extra.params)).thenReturn(list);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertEquals(list, request.getParams());
	}
	
	@Test
	public void shouldGetUid() {
		when(intent.getLongExtra(Request.Extra.uid, 0l)).thenReturn(1L);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
	}
	
	@Test
	public void shouldIsGeneratedByIntentReturnTrueIfAreReallyTheSame() {
		when(intent.getLongExtra(Request.Extra.uid, 0l)).thenReturn(1L);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
		
		assertTrue(request.isGeneratedByIntent(intent));
	}
	
	@Test
	public void shouldIsGeneratedByIntentReturnFalseWithDifferentIntent() {
		when(intent.getLongExtra(Request.Extra.uid, 0l)).thenReturn(1L);
		
		Request request = new Request(intent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
		
		Intent intent2 = mock(Intent.class);
		when(intent2.getAction()).thenReturn(ACTION);
		when(intent2.getLongExtra(Request.Extra.uid, 0l)).thenReturn(21L);
		
		assertFalse(request.isGeneratedByIntent(intent2));
	}
	
}
