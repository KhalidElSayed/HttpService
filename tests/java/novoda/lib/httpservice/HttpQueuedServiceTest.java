package novoda.lib.httpservice;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static novoda.lib.httpservice.util.Time.await;
import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.handler.BaseAsyncHandler;
import novoda.lib.httpservice.provider.LocalProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpQueuedServiceTest {

	private LocalProvider<String> provider = new LocalProvider<String>(); {
		provider.add("http://www.google.com", "ok");
	}

	private int calls; 

	private Intent mIntent;
	
	@Before
	public void setUp() {
		mIntent = mock(Intent.class);
		calls = 0;
	}

	@Ignore
	@Test
	public void shouldWork() {
		HttpQueuedService<String> service = new HttpQueuedService<String>(provider) {
			@Override
			protected AsyncHandler<String> getHandler(Intent intent) {
				return new BaseAsyncHandler<String>(String.class) {
					public void onContentReceived(String content) {
						calls++;
						assertEquals("ok", content);
					};
				};
			}

			@Override
			protected Request getRequest(Intent intent) {
				return new Request("http://www.google.com");
			}
		};
		service.startService(mIntent);
		await(200);
		assertEquals("The service was suppose to call onHandleResult once but didn't", 1, calls);
	}
	
}
