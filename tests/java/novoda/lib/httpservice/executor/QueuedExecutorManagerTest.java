package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.Time.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import android.content.Intent;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class QueuedExecutorManagerTest {
	
	private Intent mIntent;
	private Callable<String> mCallable;

	private int getCallableCalls = 0;
	
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		mCallable = mock(Callable.class);
		mIntent = mock(Intent.class);
		getCallableCalls = 0;
	}
	
	@Test
	public void shouldThrowExceptionOnTheFirstExecutionIfCallableCreatedByTheServiceIsNull() {
		QueuedExecutorManager<String> manager = new QueuedExecutorManager<String>(new CallableExecutor<String>() {
			@Override
			public Callable<String> getCallable(Intent intent) {
				return null;
			}
		});
		manager.start();
	}
	
	@Test
	public void shouldStartNormalPool() throws Exception {
		mCallable = mock(Callable.class);
		when(mCallable.call()).thenReturn("ok");
		
		QueuedExecutorManager<String> manager = new QueuedExecutorManager<String>(new CallableExecutor<String>() {
			@Override
			public Callable<String> getCallable(Intent intent) {
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
	
	@Test
	public void shouldShutdownWork() throws Exception {
		when(mCallable.call()).thenAnswer(new Answer<String>(){
			@Override
			public String answer(InvocationOnMock invocation) throws Throwable {
				Thread.sleep(500);
				return "ok";
			}
		});
		
		QueuedExecutorManager<String> manager = new QueuedExecutorManager<String>(new CallableExecutor<String>() {
			@Override
			public Callable<String> getCallable(Intent intent) {
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
