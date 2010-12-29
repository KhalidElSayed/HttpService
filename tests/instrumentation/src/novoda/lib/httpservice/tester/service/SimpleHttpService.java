package novoda.lib.httpservice.tester.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.httpservice.HttpService;
import novoda.lib.httpservice.SimpleGlobalHandler;
import novoda.lib.httpservice.SimpleRequestHandler;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.monitor.Monitor;
import novoda.lib.httpservice.tester.util.AppLogger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Simple sample on how to use the HttpService.
 * 
 * @author luigi
 *
 */
public class SimpleHttpService extends HttpService {
	
	public static final String ACTION_REQUEST = "novoda.lib.httpservice.tester.action.ACTION_REQUEST";
	
	public static final String ACTION_START_MONITOR = "novoda.lib.httpservice.tester.ACTION_STOP_MONITOR";
	
	public static final String ACTION_STOP_MONITOR = "novoda.lib.httpservice.tester.action.ACTION_START_MONITOR";
	
	private static final String ACTION_DUMP_MONITOR = "novoda.lib.httpservice.tester.action.ACTION_DUMP_MONITOR";
	
	public static final String EXTRA_DUMP_MONITOR = "novoda.lib.httpservice.tester.extra.EXTRA_DUMP_MONITOR";
	
	public static final IntentFilter MONITOR_INTENT_FILTER = new IntentFilter(ACTION_DUMP_MONITOR);

	public static final String CITIES_HANDLER = "citiesHandler";
	
	private static int globalCount = 0;
	
	private GlobalHandler globalHandler = new SimpleGlobalHandler() {
		@Override
		public void onContentReceived(Response response) {
			AppLogger.debug("Received content for global handler : " + globalCount);
			globalCount++;
		};
	};
	
	private RequestHandler requestHandler = new SimpleRequestHandler() {
		@Override
		public void onContentReceived(Response response) {
			AppLogger.debug("Received content for request handler : " + response.getContentAsString().length());
		};
	};
	
	
	@Override
	public void onCreate() {
		AppLogger.debug("creating service");
		super.onCreate();
		attach(new Monitor() {
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
		});
		registerReceiver(startMonitor, new IntentFilter(ACTION_START_MONITOR));
		registerReceiver(stopMonitor, new IntentFilter(ACTION_STOP_MONITOR));
		
		//Adding handlers with default key
		AppLogger.debug("adding handlers");
		addGlobalHandler(globalHandler);
		addRequestHandler(requestHandler);
	}
	
	@Override
	public void onDestroy() {
		AppLogger.debug("destroy on the service");
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
