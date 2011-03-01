package com.novoda.lib.httpservice.tester.service;

import static com.novoda.lib.httpservice.tester.util.Log.v;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.novoda.lib.httpservice.processor.oauth.OAuthProcessor;
import com.novoda.lib.httpservice.service.monitor.Monitor;

/**
 * Simple sample on how to use the HttpService.
 * 
 * @author luigi
 *
 */
public class SimpleHttpService extends BaseHttpService {
	
	public static final String ACTION_REQUEST = "novoda.lib.httpservice.tester.action.ACTION_REQUEST";
	
	public static final String ACTION_START_MONITOR = "novoda.lib.httpservice.tester.ACTION_STOP_MONITOR";
	
	public static final String ACTION_STOP_MONITOR = "novoda.lib.httpservice.tester.action.ACTION_START_MONITOR";
	
	private static final String ACTION_DUMP_MONITOR = "novoda.lib.httpservice.tester.action.ACTION_DUMP_MONITOR";
	
	public static final String EXTRA_DUMP_MONITOR = "novoda.lib.httpservice.tester.extra.EXTRA_DUMP_MONITOR";
	
	public static final String ACTION_CONTENT_CONSUMED = "novoda.lib.httpservice.tester.ACTION_CONTENT_CONSUMED";

	public static final IntentFilter INTENT_FILTER_CONTENT_CONSUMED = new IntentFilter(ACTION_CONTENT_CONSUMED);
	
	public static final IntentFilter INTENT_FILTER_MONITOR = new IntentFilter(ACTION_DUMP_MONITOR);
	
	private CustomProcessor processor = new CustomProcessor();
	private CustomRequestHandler handler = new CustomRequestHandler();
	
	private Monitor monitor = new Monitor() {
		@Override
		public void update(Map<String, String> properties) {
			ArrayList<String> keys = new ArrayList<String>();
			Intent intent = new Intent(ACTION_DUMP_MONITOR);
			for (Entry<String, String> entry: properties.entrySet()) {
				keys.add(entry.getKey());
				intent.putExtra(entry.getKey(), entry.getValue());
			}
			intent.putStringArrayListExtra(EXTRA_DUMP_MONITOR, keys);
			sendBroadcast(intent);
		}
		@Override
		public long getInterval() {
			return 1000;
		}
	};
	
	@Override
	public void onCreate() {
		v("creating service");
		super.onCreate();
		attach(monitor);
		registerReceiver(startMonitor, new IntentFilter(ACTION_START_MONITOR));
		registerReceiver(stopMonitor, new IntentFilter(ACTION_STOP_MONITOR));
		//Adding handlers with default key
		v("adding handlers");
		add(handler);
		add(processor);
		add(new OAuthProcessor("test", "test"));
	}

	@Override
	public void onDestroy() {
		v("destroy on the service");
		stopMonitoring();
		unregisterReceiver(startMonitor);
		unregisterReceiver(stopMonitor);
		super.onDestroy();
	}
	
	private BroadcastReceiver startMonitor = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			startMonitoring();
		}
	};
	
	public BroadcastReceiver stopMonitor = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			stopMonitoring();
		}
	};

}
