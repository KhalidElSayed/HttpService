package com.novoda.httpservice.service;

import static com.novoda.httpservice.test.Time.await;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.novoda.httpservice.Settings;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentRegistry;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.service.executor.ExecutorManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class ExecutorServiceTest {
	
	private Intent mIntent;
	
	private ExecutorService service;
	
	private IntentRegistry mRequestRegistry;
	
	private ExecutorManager mExecutorManager;
	
	private EventBus mEventBus;

	@Before
	public void setUp() {
		mIntent = mock(Intent.class);
		mRequestRegistry = mock(IntentRegistry.class);
		mExecutorManager = mock(ExecutorManager.class);
		mEventBus = mock(EventBus.class);
	}
	
    @Test
    public void shouldBeAbleToInitializeTheService() throws InterruptedException, ExecutionException {
    	service = new ExecutorService(mRequestRegistry, mEventBus, mExecutorManager, new Settings()) {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    }
    
    @Test
    public void shouldPassTheStringToOnHandleResult() throws InterruptedException, ExecutionException {
    	service = new ExecutorService(mRequestRegistry, mEventBus, mExecutorManager, new Settings()) {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    	service.startService(mIntent);
    	await(100);
    	service.stopService(mIntent);
    }

}
