package com.novoda.lib.httpservice.util;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ConnectivityReceiverTest {
	
	private Context mContext;
	
	private Intent mIntent;
	
	private boolean connectionLostCall;
	
	private boolean connectionResumedCall;
	
	private ConnectivityReceiver connectivityReceiver;
	
	@Before
	public void setup() {
		connectionLostCall = false;
		connectionResumedCall = false;
		mContext = mock(Context.class);
		mIntent = mock(Intent.class);
		connectivityReceiver = new ConnectivityReceiver() {
			@Override
			protected void onConnectionLost() {
				connectionLostCall = true;
			}

			@Override
			protected void onConnectionResume() {
				connectionResumedCall = true;
			}
		};
	}
	
	@Test
	public void shouldNotFailIfActionIsNull() {
		when(mIntent.getAction()).thenReturn(null);
		
		connectivityReceiver.onReceive(mContext, mIntent);
	}
	
	@Test
	public void shouldFireOnConnectionLost() {
		when(mIntent.getAction()).thenReturn(ConnectivityManager.CONNECTIVITY_ACTION);
		when(mIntent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)).thenReturn(true);
		
		connectivityReceiver.onReceive(mContext, mIntent);
		
		assertTrue("Connection Lost call was not fired", connectionLostCall);
	}
	
	@Test
	public void shouldFireOnConnectionResume() {
		when(mIntent.getAction()).thenReturn(ConnectivityManager.CONNECTIVITY_ACTION);
		when(mIntent.hasExtra(ConnectivityManager.EXTRA_NETWORK_INFO)).thenReturn(true);
		NetworkInfo info = mock(NetworkInfo.class);
		when(info.isConnectedOrConnecting()).thenReturn(true);
		when(mIntent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO)).thenReturn(info);
		
		connectivityReceiver.onReceive(mContext, mIntent);
		
		assertTrue("Connection resumed call was not fired", connectionResumedCall);
	}

}
