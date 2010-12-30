package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.handler.RequestHandler;

import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestHandlerEventBusTest extends EventBusTest<RequestHandler>{

	public RequestHandlerEventBusTest() {
		super(RequestHandler.class);
	}
	
}
