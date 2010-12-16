package novoda.lib.httpservice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestTest {
	
	@Test
	public void shouldCreateAFullRequestJustWithTheUrl() {
		Request request = new Request("http://www.google.com");
		assertEquals("http://www.google.com", request.getUrl());
	}
	
	@Test
	public void shouldCreateAFullRequestJustWithTheUrlEvenWithNotSpecifiedProtocol() {
		Request request = new Request("www.google.com");
		assertEquals("http://www.google.com", request.getUrl());
	}

	@Test(expected = RequestException.class)
	public void shouldThrowAsyncHttpExceptionOnNullUrl() {
		String nullUrl = null;
		new Request(nullUrl);
	}

}
