package novoda.lib.httpservice.provider.http;

import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpProviderTest {
	
	private Provider provider;
	
	@Before
	public void setUp() {
		provider  = new HttpProvider();
	}
	
	@Ignore //Robolectric doesn't seams to support http  
	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		provider.execute(new Request("http://www.google.com"));
	}

}
