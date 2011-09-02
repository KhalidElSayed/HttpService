package com.novoda.httpservice.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.novoda.httpservice.exception.RequestException;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.util.IntentBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class IntentWrapperTest {

	private static final String ACTION = "ACTION";
	
	private Intent mIntent;
	
	@Before
	public void setUpValidDefaultRequest() {
		mIntent = mock(Intent.class);
		when(mIntent.getAction()).thenReturn(ACTION);
		
		Uri uri = mock(Uri.class);
		when(mIntent.getData()).thenReturn(uri);
	}
	
	@Test
	public void shouldIsGetRespondTrueIfSettedAccondingly() {
		Intent intent = new IntentBuilder(ACTION, "http://www.google.com").build();
		
		IntentWrapper r = new IntentWrapper(intent);

		assertTrue(r.isGet());
		assertFalse(r.isPost());
	}
	
	@Test
	public void shouldIsPostRespondTrueIfSettedAccondingly() {
		Intent intent = new IntentBuilder("", "").asPost().build();
		
		IntentWrapper r = new IntentWrapper(intent);

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
		
		URI result = IntentWrapper.asURI(uri);
		
		assertNotNull(result);
		assertEquals("http", result.getScheme());
		assertEquals("www.google.com", result.getHost());
		assertEquals(80, result.getPort());
		assertEquals("/relative/path", uri.getEncodedPath());
		assertEquals("", uri.getQuery());
		assertEquals("", uri.getFragment());
	}
	
	@Test(expected = RequestException.class)
	public void shouldThrowRequestExceptionIfIntentIsNull() {
		new IntentWrapper(null);
	}
	
	@Test
	public void shouldBuildSimpleHttpRequestDoesNotFailIfUrlAndUriAreNotProvided() {
		Intent intent = mock(Intent.class);
		
		new IntentWrapper(intent);
	}
	
	@Test
	public void shoudBuildRequestFromIntentWithOnlyUri() {
		Uri uri = mock(Uri.class);
		when(mIntent.getData()).thenReturn(uri);

		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertEquals(uri, request.getUri());
	}
	
	@Test
	public void shouldSetTheRequestReceiverIfThere() {
		ResultReceiver expectedReceived = mock(ResultReceiver.class);
		when(mIntent.getParcelableExtra(IntentWrapper.Extra.result_receiver)).thenReturn(expectedReceived);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		ResultReceiver actualReceived = request.getResultReceiver();
		assertNotNull(actualReceived);
		assertEquals(expectedReceived, actualReceived);
	}
	
	@Test
	public void shouldBuildRequestWithGetMethodByDefault() {
		when(mIntent.getIntExtra(IntentWrapper.Extra.method, 
				IntentWrapper.Method.GET)).thenReturn(IntentWrapper.Method.GET);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertTrue(request.isGet());
	}
	
	@Test
	public void shouldBuildWithPostMethod() {
		when(mIntent.getIntExtra(IntentWrapper.Extra.method, 
				IntentWrapper.Method.GET)).thenReturn(IntentWrapper.Method.POST);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertTrue(request.isPost());
	}
	
	@Test
	public void shouldReadTheHandlerKey() {
		when(mIntent.getStringExtra(IntentWrapper.Extra.handler_key)).thenReturn("specificHandler");
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertEquals("specificHandler", request.getHandlerKey());
	}
	
	@Test
	public void shouldReadParams() {
		ArrayList<Parcelable> list = new ArrayList<Parcelable>();
		when(mIntent.getParcelableArrayListExtra(IntentWrapper.Extra.params)).thenReturn(list);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertEquals(list, request.getParams());
	}
	
	@Test
	public void shouldGetUid() {
		when(mIntent.getLongExtra(IntentWrapper.Extra.uid, 0l)).thenReturn(1L);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
	}
	
	@Test
	public void shouldIsGeneratedByIntentReturnTrueIfAreReallyTheSame() {
		when(mIntent.getLongExtra(IntentWrapper.Extra.uid, 0l)).thenReturn(1L);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
		
		assertTrue(request.isGeneratedByIntent(mIntent));
	}
	
	@Test
	public void shouldIsGeneratedByIntentReturnFalseWithDifferentIntent() {
		when(mIntent.getLongExtra(IntentWrapper.Extra.uid, 0l)).thenReturn(1L);
		
		IntentWrapper request = new IntentWrapper(mIntent);
		
		assertNotNull(request);
		assertTrue(request.getUid() > 0l);
		
		Intent intent2 = mock(Intent.class);
		when(intent2.getAction()).thenReturn(ACTION);
		when(intent2.getLongExtra(IntentWrapper.Extra.uid, 0l)).thenReturn(21L);
		
		assertFalse(request.isGeneratedByIntent(intent2));
	}
	
	@Test
	public void shouldAsSameReturnTrueIfRequestHaveSameUrl() throws URISyntaxException {
		Uri uri = mock(Uri.class);
		when(uri.compareTo(any(Uri.class))).thenReturn(0);
		
		IntentWrapper r1 = mock(IntentWrapper.class);
		when(r1.getUri()).thenReturn(uri);
		when(r1.sameAs(any(IntentWrapper.class))).thenCallRealMethod();
		when(r1.asURI()).thenReturn(new URI("http://www.google.com"));
		
		IntentWrapper r2 = mock(IntentWrapper.class);
		when(r2.getUri()).thenReturn(uri);
		when(r2.asURI()).thenReturn(new URI("http://www.google.com"));
		
		assertTrue(r1.sameAs(r2));
	}
	
	@Test
	public void shouldAsSameReturnFalseIfRequestHaveDifferentUri() {
		Uri uri2 = mock(Uri.class);

		Uri uri1 = mock(Uri.class);
		when(uri1.compareTo(uri2)).thenReturn(1);
		
		IntentWrapper r1 = mock(IntentWrapper.class);
		when(r1.getUri()).thenReturn(uri1);
		when(r1.sameAs(any(IntentWrapper.class))).thenCallRealMethod();
		
		IntentWrapper r2 = mock(IntentWrapper.class);
		when(r2.getUri()).thenReturn(uri2);
		
		assertFalse(r1.sameAs(r2));
	}
	
}
