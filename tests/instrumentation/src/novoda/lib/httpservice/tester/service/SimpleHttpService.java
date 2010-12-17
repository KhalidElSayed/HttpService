package novoda.lib.httpservice.tester.service;

import java.util.Map;
import java.util.Map.Entry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import novoda.lib.httpservice.HttpQueuedService;
import novoda.lib.httpservice.executor.Monitor;
import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.handler.BaseAsyncHandler;
import novoda.lib.httpservice.tester.util.AppLogger;

public class SimpleHttpService extends HttpQueuedService<String> {
	
	public static final String START_MONITOR_ACTION = "novoda.lib.httpservice.tester.actio.STOP_MONITOR";
	
	public static final String STOP_MONITOR_ACTION = "novoda.lib.httpservice.tester.actio.START_MONITOR";

	private AsyncHandler<String> handler = new BaseAsyncHandler<String>(String.class) {
		@Override
		public void onContentReceived(String content) {
			//AppLogger.debug("Content received : " + content);
		}

		@Override
		public void onThrowable(Throwable t) {
			AppLogger.logVisibly("There was an exception during the call : " + t);
		}
	};
	
	@Override
	protected AsyncHandler<String> getHandler() {
		return handler;
	}
	
	public void onCreate() {
		attach(new Monitor() {
			@Override
			public void dump(Map<String, String> properties) {
				StringBuilder builder = new StringBuilder("Monitoring[ | ");
				for (Entry<String, String> entry: properties.entrySet()) {
					builder.append(entry.getKey()).append(":").append(entry.getValue()).append(" | ");
				}
				AppLogger.debug(builder.append("]").toString());
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
