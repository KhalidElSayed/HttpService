package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.Time.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import novoda.lib.httpservice.handler.BaseAsyncHandler;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocalProviderTest {
	
	private LocalProvider<String> provider;
	
	private boolean async = false;
	
	@Before
	public void setUp() {
		provider  = new LocalProvider<String>();
		provider.add("http://www.google.com", "ok");
	}
	
	@Test
	public void shouldBasicHttpProviderGoAndFetchSomeUrlContent() {
		provider.execute(new Request("http://www.google.com"), new BaseAsyncHandler<String>(String.class) {
			@Override
			public void onContentReceived(String content) {
				async = true;
				assertEquals("ok", content);
			}
		});
		await(100);
		assertTrue(async);
		async = false;
	}

}
