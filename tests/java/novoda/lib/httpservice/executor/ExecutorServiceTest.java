package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.Time.await;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.os.Handler;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExecutorServiceTest {
	
	private Intent mIntent = mock(Intent.class);
	
	private ExecutorService<String> service;
	
	private int calls = 0;
	
	@Before
	public void setUp() {
		calls = 0;
		service = new ExecutorService<String>() {
			@Override
			public Callable<String> getCallable(Intent intent) {
				// TODO Auto-generated method stub
				return null;
			}
    	};
	}
	
    @Test
    public void shouldBeAbleToInitializeTheService() throws InterruptedException, ExecutionException {
    	service.startService(mIntent);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void shouldPassTheStringToOnHandleResult() throws InterruptedException, ExecutionException {
    	Handler mHandler = mock(Handler.class);
    	ExecutorManager<String> mExecutorManager = mock(ExecutorManager.class);
    	service = new ExecutorService<String>(mExecutorManager, mHandler) {
			@Override
			public Callable<String> getCallable(Intent intent) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    	service.startService(mIntent);
    	await(100);
    	service.stopService(mIntent);
    }
    
    @Ignore
    @Test
    public void shouldShutdownAfterAWhile() throws InterruptedException, ExecutionException {
    	service = new ExecutorService<String>() {
			@Override
			public Callable<String> getCallable(Intent intent) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
			
			@Override
			public void onDestroy() {
				calls++;
			}
			
    	};
    	service.startService(mIntent);
    	assertEquals("destroy shouldn't be called straight away", 0, calls);
    	await(100);
    	assertEquals("destroy shouldn't be after 100 mill", 0, calls);
    	await(100);
    	service.stopSelf();
    	
    	await(300);
    	assertEquals("should have called destroy", 1, calls);
    }

}
