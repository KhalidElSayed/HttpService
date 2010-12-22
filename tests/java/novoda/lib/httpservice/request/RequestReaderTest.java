package novoda.lib.httpservice.request;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.HttpServiceConstant;
import novoda.lib.httpservice.exception.RequestException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestReaderTest { 
	
	private static final String URL = "http://www.google.com";
	
	private Intent intent;
	
	@Before
	public void setUpValidDefaultRequest() {
		intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.request);
		when(intent.getStringExtra(HttpServiceConstant.Extra.url)).thenReturn(URL);
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
		when(intent.getAction()).thenReturn(HttpServiceConstant.request);
		
		RequestReader.read(intent);
	}
	
	@Test
	public void shouldBuildRequestFromIntentWithOnlyUrl() {
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertEquals(URL, request.getUrl());
	}
	
	@Test
	public void shoudBuildRequestFromIntentWithOnlyUri() {
		when(intent.getStringExtra(HttpServiceConstant.Extra.url)).thenReturn(null);
		Uri uri = mock(Uri.class);
		when(intent.getData()).thenReturn(uri);

		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertEquals(uri, request.getUri());
	}
	
	@Test
	public void shouldSetTheRequestReceiverIfThere() {
		ResultReceiver expectedReceived = mock(ResultReceiver.class);
		when(intent.getParcelableExtra(HttpServiceConstant.Extra.result_receiver)).thenReturn(expectedReceived);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		ResultReceiver actualReceived = request.getResultReceiver();
		assertNotNull(actualReceived);
		assertEquals(expectedReceived, actualReceived);
	}
	
	@Test
	public void shouldBuildRequestWithGetMethodByDefault() {
		when(intent.getIntExtra(HttpServiceConstant.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.GET);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertTrue(request.isGet());
	}
	
	@Test
	public void shouldBuildWithPostMethod() {
		when(intent.getIntExtra(HttpServiceConstant.Extra.method, 
				Request.Method.GET)).thenReturn(Request.Method.POST);
		
		Request request = RequestReader.read(intent);
		
		assertNotNull(request);
		assertTrue(request.isPost());
	}

}
