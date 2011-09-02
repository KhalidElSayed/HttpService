package com.novoda.httpservice.service.executor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.novoda.httpservice.Settings;
import com.novoda.httpservice.util.ConnectivityReceiver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.app.Service;
import android.content.IntentFilter;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class ConnectedThreadPoolExecutorTest {
	
	private Service mService;
	
	private ConnectedThreadPoolExecutor pool;
	
	@Before
	public void setup() {
		mService = mock(Service.class);
		pool = new ConnectedThreadPoolExecutor(mService, new Settings());
	}
	
	@Test
	public void shouldRegisterReceiversonStart() {
		pool.start();
		
		verify(mService, times(2)).registerReceiver(any(ConnectivityReceiver.class), any(IntentFilter.class));
	}
	
	@Test
	public void shouldUnregisterReceiversonShutdown() {
		pool.shutdown();
		
		verify(mService, times(1)).unregisterReceiver(any(ConnectivityReceiver.class));
	}
	
	@Test
	public void shouldUnregisterReceiversonShutdownNow() {
		pool.shutdownNow();
		
		verify(mService, times(1)).unregisterReceiver(any(ConnectivityReceiver.class));
	}

}
