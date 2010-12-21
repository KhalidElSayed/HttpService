package novoda.lib.httpservice.tester.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.httpservice.HttpService;
import novoda.lib.httpservice.executor.monitor.Monitor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SimpleHttpService extends HttpService<String> {
	
	public static final String START_MONITOR_ACTION = "novoda.lib.httpservice.tester.action.STOP_MONITOR";
	
	public static final String STOP_MONITOR_ACTION = "novoda.lib.httpservice.tester.action.START_MONITOR";
	
	private static final String DUMP_MONITOR_ACTION = "novoda.lib.httpservice.tester.action.DUMP_MONITOR";
	
	public static final String DUMP_MONITOR_EXTRA = "novoda.lib.httpservice.tester.extra.DUMP_MONITOR";
	
	public static final IntentFilter MONITOR_INTENT_FILTER = new IntentFilter(DUMP_MONITOR_ACTION);

//	private AsyncHandler<String> handler = new BaseAsyncHandler<String>(String.class) {
//		@Override
//		public void onContentReceived(String content) {
//			//AppLogger.debug("Content received : " + content);
//		}
//
//		@Override
//		public void onThrowable(Throwable t) {
//			AppLogger.logVisibly("There was an exception during the call : " + t);
//		}
//	};
	
	@Override
	public void onCreate() {
		attach(new Monitor() {
			@Override
			public void dump(Map<String, String> properties) {
				ArrayList<String> keys = new ArrayList<String>();
				Intent intent = new Intent(DUMP_MONITOR_ACTION);
				for (Entry<String, String> entry: properties.entrySet()) {
					keys.add(entry.getKey());
					intent.putExtra(entry.getKey(), entry.getValue());
				}
				intent.putStringArrayListExtra(DUMP_MONITOR_EXTRA, keys);
				sendBroadcast(intent);
			}

			@Override
			public long getInterval() {
				return 1000;
			}		
		});
		registerReceiver(startMonitor, new IntentFilter(START_MONITOR_ACTION));
		registerReceiver(stopMonitor, new IntentFilter(STOP_MONITOR_ACTION));
	}
	
	@Override
	public void onDestroy() {
		stopMonitoring();
		unregisterReceiver(startMonitor);
		unregisterReceiver(stopMonitor);
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
