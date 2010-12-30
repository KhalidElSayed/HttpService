package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.handler.GlobalHandler;

import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class GlobalHandlerEventBusTest extends EventBusTest<GlobalHandler>{

	public GlobalHandlerEventBusTest() {
		super(GlobalHandler.class);
	}
	
}
