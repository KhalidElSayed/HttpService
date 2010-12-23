package novoda.lib.httpservice.request;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import novoda.lib.httpservice.exception.RequestException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestReaderTest { 
	
	private Intent intent;
	
	@Before
	public void setUpValidDefaultRequest() {
		intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(Request.Action.request);
		Uri uri = mock(Uri.class);
		when(intent.getData()).thenReturn(uri);
	}
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfTheActionIsNull() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(null);
		
		RequestReader.read(intent);
	}
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfTheActionIsNotImplemented() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn("action.not.implemented");
		
		RequestReader.read(intent);
	}
	
	@Test(expected = RequestException.class)
	public void shouldBuildSimpleHttpRequestFaildWithExceptionIfUrlAndUriAreNotProvided() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(Request.Action.request);
		
		RequestReader.read(intent);
	}
	
	@Test
	public void shoudBuildRequestFromIntentWithOnlyUri() {
		Uri uri = mock(Uri.class);
		when(intent.getData()).thenReturn(uri);

		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertEquals(uri, request.getUri());
	}
	
	@Test
	public void shouldSetTheRequestReceiverIfThere() {
		ResultReceiver expectedReceived = mock(ResultReceiver.class);
		when(intent.getParcelableExtra(Request.Extra.result_receiver)).thenReturn(expectedReceived);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		ResultReceiver actualReceived = request.getResultReceiver();
		assertNotNull(actualReceived);
		assertEquals(expectedReceived, actualReceived);
	}
	
	@Test
	public void shouldBuildRequestWithGetMethodByDefault() {
		when(intent.getIntExtra(Request.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.GET);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertTrue(request.isGet());
	}
	
	@Test
	public void shouldBuildWithPostMethod() {
		when(intent.getIntExtra(Request.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.POST);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertTrue(request.isPost());
	}
	
	@Test
	public void shouldReadTheHandlerKey() {
		when(intent.getStringExtra(Request.Extra.handler_key)).thenReturn("specificHandler");
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertEquals("specificHandler", request.getHandlerKey());
	}
	
	@Test
	public void shouldReadParams() {
		ArrayList<Parcelable> list = new ArrayList<Parcelable>();
		when(intent.getParcelableArrayListExtra(Request.Extra.params)).thenReturn(list);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertEquals(list, request.getParams());
	}

}
