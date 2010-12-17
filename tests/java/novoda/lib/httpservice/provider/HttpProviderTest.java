package novoda.lib.httpservice.provider;

import static org.junit.Assert.assertNotNull;
import novoda.lib.httpservice.handler.BaseAsyncHandler;
import novoda.lib.httpservice.provider.HttpProvider;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpProviderTest {
	
	private Provider<String> provider;
	
	@Before
	public void setUp() {
		provider  = new HttpProvider<String>();
	}
	
	@Ignore //Robolectric doesn't seams to support http  
	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		provider.execute(new Request("http://www.google.com"), new BaseAsyncHandler<String>(String.class) {
			@Override
			public void onContentReceived(String content) {
				assertNotNull(content);
			}
		});
	}

}
