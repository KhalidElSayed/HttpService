package novoda.lib.httpservice;


import static novoda.lib.httpservice.util.Time.await;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import novoda.lib.httpservice.provider.local.LocalProvider;
import novoda.lib.httpservice.request.Request;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HttpServiceTest {

	private LocalProvider provider = new LocalProvider(); {
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
		HttpService<String> service = new HttpService<String>(provider) {
//			@Override
//			protected AsyncHandler<String> getHandler() {
//				return new BaseAsyncHandler<String>(String.class) {
//					public void onContentReceived(String content) {
//						calls++;
//						assertEquals("ok", content);
//					};
//				};
//			}

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
