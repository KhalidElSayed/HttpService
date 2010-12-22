package novoda.lib.httpservice.request;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.HttpServiceConstant;
import novoda.lib.httpservice.exception.RequestException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestBuilderTest { 
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfTheActionIsNull() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(null);
		
		RequestBuilder.build(intent);
	}
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfTheActionIsNotImplemented() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn("action.not.implemented");
		
		RequestBuilder.build(intent);
	}
	
	@Test(expected = RequestException.class)
	public void shouldBuildSimpleHttpRequestFaildWithExceptionIfUrlIsNotProvided() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.simple_request);
		when(intent.getStringExtra(HttpServiceConstant.Extra.url)).thenReturn(null);
		
		RequestBuilder.build(intent);
	}
	
	@Ignore
	@Test
	public void shouldBuildRequestFromSimpleIntent() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.simple_request);
		when(intent.getStringExtra(HttpServiceConstant.Extra.url)).thenReturn("http://www.google.com");
		
		Request request = RequestBuilder.build(intent);
		
		assertNotNull(request);
		assertEquals("http://www.google.com", request.getUrl());
	}
	
	@Test(expected = RequestException.class)
	public void shoudBuildForUriThrowRequestExceptionIfUriIsNotSetted() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.uri_request);
		when(intent.getData()).thenReturn(null);
		
		RequestBuilder.build(intent);
	}
	
	@Test
	public void shouldSetTheRequestReceiverIfThere() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.uri_request);
		when(intent.getAction()).thenReturn(HttpServiceConstant.simple_request);
		when(intent.getStringExtra(HttpServiceConstant.Extra.url)).thenReturn("http://www.google.com");
		
		RequestReceiver rrSent = mock(RequestReceiver.class); 
		when(intent.hasExtra(HttpServiceConstant.Extra.request_parcable)).thenReturn(true);
		when(intent.getParcelableExtra(HttpServiceConstant.Extra.request_parcable)).thenReturn(rrSent);
		
		Request request = RequestBuilder.build(intent);
		
		assertNotNull(request);
		assertEquals("http://www.google.com", request.getUrl());
		ResultReceiver rrReceived = request.getResultReceiver();
		assertNotNull(rrReceived);
	}
	
	@Ignore
	@Test
	public void shouldBuildForUri() {
		Intent intent = mock(Intent.class);
		when(intent.getAction()).thenReturn(HttpServiceConstant.uri_request);

		Uri data = Uri.parse("http://www.google.com");
		when(intent.getData()).thenReturn(data);
		
		Request request = RequestBuilder.build(intent);
		
		assertNotNull(request);
		assertEquals("http://www.google.com", request.getUri());
	}
	
	@Ignore
	@Test
	public void shouldWork() {
		Uri data = Uri.parse("http://www.google.com");
		
		assertEquals("http", data.getScheme());
		assertEquals("www.google.com", data.getHost());
		assertEquals("http", data.getScheme());
		assertEquals("http", data.getScheme());
		
		//TODO I think the shadow for Uri is not working
		// check in the robolectrict
		
//		URIUtils.createURI(uri.getScheme(), uri.getHost(),
//				uri.getPort(), uri.getEncodedPath(), uri.getQuery(), uri
//						.getFragment())
						
						
	}
	
	@Ignore
	@Test
	public void shouldBuildForParcable() {
//		Intent intent;
//		intent.putExtra("novoda.lib.httpservice.request", new HttpGetParcelabl())ò

	}

}
