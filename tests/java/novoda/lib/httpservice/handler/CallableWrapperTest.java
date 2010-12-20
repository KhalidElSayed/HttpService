package novoda.lib.httpservice.handler;

import novoda.lib.httpservice.provider.local.LocalProvider;
import novoda.lib.httpservice.request.Request;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CallableWrapperTest {

	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfRequestIsNull() throws Exception {
		new CallableWrapper<String>(new BaseAsyncHandler<String>(String.class), new LocalProvider<String>(), null).call();
	}
	
	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfLocalProviderIsNull() throws Exception {
		new CallableWrapper<String>(new BaseAsyncHandler<String>(String.class), null, new Request("www.google.com")).call();
	}
	
	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfHandlerIsNull() throws Exception {
		new CallableWrapper<String>(null, new LocalProvider<String>(), new Request("www.google.com")).call();
	}
	
}
