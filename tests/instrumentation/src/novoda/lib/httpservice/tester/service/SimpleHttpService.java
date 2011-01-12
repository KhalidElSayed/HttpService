package novoda.lib.httpservice.tester.service;

import static novoda.lib.httpservice.tester.util.HttpServiceTesterLog.Default.d;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import novoda.lib.httpservice.HttpService;
import novoda.lib.httpservice.SimpleRequestHandler;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.processor.oauth.OAuthProcessor;
import novoda.lib.httpservice.provider.IntentWrapper;
import novoda.lib.httpservice.provider.Response;
import novoda.lib.httpservice.service.monitor.Monitor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

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
	
	public static final String ACTION_CONTENT_CONSUMED = "novoda.lib.httpservice.tester.ACTION_CONTENT_CONSUMED";

	public static final IntentFilter INTENT_FILTER_CONTENT_CONSUMED = new IntentFilter(ACTION_CONTENT_CONSUMED);
	
	public static final IntentFilter INTENT_FILTER_MONITOR = new IntentFilter(ACTION_DUMP_MONITOR);
	
	private static final String PATH = "/";
	
	private RequestHandler requestHandler = new SimpleRequestHandler() {
		@Override
		public boolean match(IntentWrapper intentWrapper) {
			if(PATH.equals(intentWrapper.getUri().getPath())) {
				d("do match!");
				return true;
			}
			d("doesn't match!");
			return false;
		}
		
		@Override
		public void onContentReceived(IntentWrapper intentWrapper, Response response) {
			d("Received content for request handler : " + response.getContentAsString().length());
		};
	};
	
	private Processor logProcessor1 = new Processor() {
		@Override
		public boolean match(IntentWrapper intentWrapper) {
			return true;
		}

		@Override
		public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
			d("Processing request 1...");
		}

		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			d("Processing response 1...");	
		}
		
	};

	private Processor logProcessor2 = new Processor() {
		@Override
		public boolean match(IntentWrapper intentWrapper) {
			return true;
		}

		@Override
		public void process(HttpRequest response, HttpContext context) throws HttpException, IOException {
			d("Processing request 2...");
		}

		@Override
		public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
			d("Processing response 2...");	
		}
	};
	
	@Override
	public void onCreate() {
		d("creating service");
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
		d("adding handlers");
		add(requestHandler);
		
		add(logProcessor1);
		add(logProcessor2);
		add(new OAuthProcessor("test", "test"));
	}
	
	@Override
	public void onDestroy() {
		d("destroy on the service");
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
