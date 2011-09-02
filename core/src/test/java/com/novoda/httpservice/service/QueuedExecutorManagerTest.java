package com.novoda.httpservice.service;

import static com.novoda.httpservice.test.Time.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentRegistry;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.service.executor.CallableExecutor;
import com.novoda.httpservice.service.executor.ThreadManager;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class QueuedExecutorManagerTest {
	
	private Intent mIntent;
	private Callable<Response> mCallable;
	private EventBus mEventBus;
	private ThreadPoolExecutor mThreadPoolExecutor;
	private IntentRegistry mRequestRegistry;
	private int getCallableCalls;
	private Response mResponse;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		mCallable = mock(Callable.class);
		mIntent = mock(Intent.class);
		mEventBus = mock(EventBus.class);
		mThreadPoolExecutor = mock(ThreadPoolExecutor.class);
		mRequestRegistry = mock(IntentRegistry.class);
		getCallableCalls = 0;
		mResponse = mock(Response.class);
	}
	
	@Test
	public void shouldThrowExceptionOnTheFirstExecutionIfCallableCreatedByTheServiceIsNull() {
		ThreadManager manager = new ThreadManager(mRequestRegistry, mThreadPoolExecutor, mEventBus, new CallableExecutor<Response>() {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				return null;
			}
		});
		manager.start();
	}
	
	@Ignore
	@Test
	public void shouldStartNormalPool() throws Exception {
		when(mCallable.call()).thenReturn(mResponse);
		
		ThreadManager manager = new ThreadManager(mRequestRegistry, mThreadPoolExecutor, mEventBus, new CallableExecutor<Response>() {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				getCallableCalls++;
				return mCallable;
			}
		});
		
		manager.addTask(mIntent);
		
		manager.start();
		
		await(100);
    	manager.shutdown();
		
		assertEquals("Get Callable didn't receive the calls as expected", 1, getCallableCalls);
		
//		verify(mCallable2);
	}
	
	@Ignore
	@Test
	public void shouldShutdownWork() throws Exception {
		when(mCallable.call()).thenAnswer(new Answer<String>(){
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				Thread.sleep(500);
				return "ok";
			}
		});
		
		ThreadManager manager = new ThreadManager(mRequestRegistry, mThreadPoolExecutor, mEventBus, new CallableExecutor<Response>() {
			@Override
			public Callable<Response> getCallable(IntentWrapper request) {
				getCallableCalls++;
				await(100);
				return mCallable;
			}
		});
		
		manager.addTask(mIntent);
		manager.start();		
		
		assertTrue("Manager is not running", manager.isWorking());
    	manager.shutdown();
    	await(100);
    	assertFalse("Manager is not still running", manager.isWorking());
	}

}
