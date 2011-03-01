package com.novoda.lib.httpservice.service;

import static com.novoda.lib.httpservice.test.Time.await;
import static org.mockito.Mockito.mock;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.novoda.lib.httpservice.provider.EventBus;
import com.novoda.lib.httpservice.provider.IntentRegistry;
import com.novoda.lib.httpservice.provider.IntentWrapper;
import com.novoda.lib.httpservice.provider.Response;
import com.novoda.lib.httpservice.service.executor.ExecutorManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

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
    	service = new ExecutorService(mRequestRegistry, mEventBus, mExecutorManager) {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				// here there should be the logic to use the httpclient and grab the content from the intent
				return null;
			}
    	};
    }
    
    @Test
    public void shouldPassTheStringToOnHandleResult() throws InterruptedException, ExecutionException {
    	service = new ExecutorService(mRequestRegistry, mEventBus, mExecutorManager) {
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
