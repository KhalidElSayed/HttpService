package novoda.lib.httpservice.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestTest {

	@Test
	public void shouldIsGetRespondTrueIfSettedAccondingly() {
		Request r = new Request();
		r.setMethod(Request.Method.GET);
		assertTrue(r.isGet());
		assertFalse(r.isPost());
	}
	
	@Test
	public void shouldIsPostRespondTrueIfSettedAccondingly() {
		Request r = new Request();
		r.setMethod(Request.Method.POST);
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

}
