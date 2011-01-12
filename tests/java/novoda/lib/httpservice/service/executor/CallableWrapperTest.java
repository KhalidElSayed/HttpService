package novoda.lib.httpservice.service.executor;

import static org.mockito.Mockito.mock;

import novoda.lib.httpservice.exception.HandlerException;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.IntentWrapper;
import novoda.lib.httpservice.provider.local.LocalProvider;
import novoda.lib.httpservice.util.IntentBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class CallableWrapperTest {
	
	private EventBus mEventBus;
	
	@Before
	public void setUp() {
		mEventBus = mock(EventBus.class);
	}

	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfRequestIsNull() throws Exception {
		new CallableWrapper(new LocalProvider(mEventBus), null).call();
	}
	
	@Test(expected = HandlerException.class)
	public void shouldThrowExceptionIfLocalProviderIsNull() throws Exception {
		new CallableWrapper(null, new IntentWrapper(new IntentBuilder("test","http://www.google.com").build())).call();
	}
	
}
