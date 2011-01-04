package novoda.lib.httpservice.tester.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

import novoda.lib.httpservice.HttpService;
import novoda.lib.httpservice.SimpleRequestHandler;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.monitor.Monitor;
import novoda.lib.httpservice.tester.util.AppLogger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

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
	
	private static final String PATH = "/";
	
	private RequestHandler requestHandler = new SimpleRequestHandler() {
		@Override
		public boolean match(Uri uri) {
			if(PATH.equals(uri.getPath())) {
				AppLogger.debug("do match!");
				return true;
			}
			AppLogger.debug("doesn't match!");
			return false;
		}
		
		@Override
		public void onContentReceived(Response response) {
			AppLogger.debug("Received content for request handler : " + response.getContentAsString().length());
		};
	};
	
	private Processor logProcessor1 = new Processor() {
		@Override
		public boolean match(Uri uri) {
			return true;
		}

		@Override
		public void process(HttpRequest arg0, HttpContext arg1) throws HttpException, IOException {
			AppLogger.debug("Processing request 1...");
		}

		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			AppLogger.debug("Processing response 1...");	
		}
		
	};

	private Processor logProcessor2 = new Processor() {
		@Override
		public boolean match(Uri uri) {
			return true;
		}

		@Override
		public void process(HttpRequest arg0, HttpContext arg1) throws HttpException, IOException {
			AppLogger.debug("Processing request 2...");
		}

		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			AppLogger.debug("Processing response 2...");	
		}
		
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
		add(requestHandler);
		
		add(logProcessor1);
		add(logProcessor2);
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
