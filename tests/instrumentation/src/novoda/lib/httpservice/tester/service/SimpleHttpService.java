package novoda.lib.httpservice.tester.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.httpservice.HttpService;
import novoda.lib.httpservice.executor.monitor.Monitor;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.handler.SimpleGlobalHandler;
import novoda.lib.httpservice.handler.SimpleRequestHandler;
import novoda.lib.httpservice.tester.util.AppLogger;
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

	public static final String CITIES_HANDLER = "citiesHandler";
	
	private static int globalCount = 0;
	
	private GlobalHandler globalHandler = new SimpleGlobalHandler() {
		@Override
		public void onContentReceived(java.io.InputStream content) {
			AppLogger.debug("Received content for global handler : " + globalCount);
			if(content != null) {
				try {
					content.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}
		};
	};
	
	private RequestHandler requestHandler = new SimpleRequestHandler() {
		@Override
		public void onContentReceived(java.io.InputStream content) {
			AppLogger.debug("Received content for request handler");
			if(content != null) {
				try {
					content.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			}
		};
	};
	
	private GlobalHandler citiesGlobalHandler = new SimpleGlobalHandler() {
		@Override
		public void onContentReceived(java.io.InputStream content) {
			if(content != null) {
				AppLogger.debug("Received cities for global handler");
				try {
					content.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			} else {
				AppLogger.debug("Received cities for global handler but content is null");
			}
		};
	};
	
	private RequestHandler citiesRequestHandler = new SimpleRequestHandler() {
		@Override
		public void onContentReceived(java.io.InputStream content) {
			if(content != null) {
				AppLogger.debug("Received cities for request handler");
				try {
					content.close();
				} catch (IOException e) {
					AppLogger.error(e);
				}
			} else {
				AppLogger.debug("Received cities for request handler but content is null");
			}
		};
	};
	
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
		
		//Adding handlers with default key
		addGlobalHandler(globalHandler);
		addRequestHandler(requestHandler);
		
		//Adding handlers with specific key
		addGlobalHandler(CITIES_HANDLER, citiesGlobalHandler);
		addRequestHandler(CITIES_HANDLER, citiesRequestHandler);
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
