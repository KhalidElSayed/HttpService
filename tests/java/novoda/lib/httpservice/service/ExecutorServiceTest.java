package novoda.lib.httpservice.service;

import static novoda.lib.httpservice.test.Time.await;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.executor.ExecutorManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExecutorServiceTest {
	
	private Intent mIntent = mock(Intent.class);
	
	private ExecutorService service;
	
	@Before
	public void setUp() {
	}
	
    @Test
    public void shouldBeAbleToInitializeTheService() throws InterruptedException, ExecutionException {
    	ExecutorManager mExecutorManager = mock(ExecutorManager.class);
    	EventBus mEventBus = mock(EventBus.class);
    	service = new ExecutorService(mEventBus, mExecutorManager) {
			@Override
			public Callable<Response> getCallable(Intent intent) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    }
    
    @Test
    public void shouldPassTheStringToOnHandleResult() throws InterruptedException, ExecutionException {
    	ExecutorManager mExecutorManager = mock(ExecutorManager.class);
    	EventBus mEventBus = mock(EventBus.class);
    	service = new ExecutorService(mEventBus, mExecutorManager) {
			@Override
			public Callable<Response> getCallable(Intent intent) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    	service.startService(mIntent);
    	await(100);
    	service.stopService(mIntent);
    }

}
