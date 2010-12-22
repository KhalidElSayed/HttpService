package novoda.lib.httpservice.executor;

import novoda.lib.httpservice.exception.HandlerException;
import novoda.lib.httpservice.executor.CallableWrapper;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.local.LocalProvider;
import novoda.lib.httpservice.request.Request;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CallableWrapperTest {

	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfRequestIsNull() throws Exception {
		new CallableWrapper<String>(new LocalProvider(new EventBus()), null).call();
	}
	
	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfLocalProviderIsNull() throws Exception {
		new CallableWrapper<String>(null, new Request("www.google.com")).call();
	}
	
}
