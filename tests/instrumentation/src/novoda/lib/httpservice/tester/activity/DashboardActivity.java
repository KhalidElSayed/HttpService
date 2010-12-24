package novoda.lib.httpservice.tester.activity;

import java.util.ArrayList;

import novoda.lib.httpservice.request.IntentRequestBuilder;
import novoda.lib.httpservice.tester.R;
import novoda.lib.httpservice.tester.service.SimpleHttpService;
import novoda.lib.httpservice.tester.util.AppLogger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DashboardActivity extends BaseActivity {
	
	private TextView monitorInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		final EditText edit = ((EditText) findViewById(R.id.requestNumber));
		final Button call = ((Button)findViewById(R.id.start));
		final Button start = ((Button)findViewById(R.id.startMonitor));
		final Button stop = ((Button)findViewById(R.id.stopMonitor));
//		final Button log = ((Button)findViewById(R.id.log));
		edit.setText("1");
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
				AppLogger.debug("Making " + text + " calls");
				for(int i= 0; i<Integer.valueOf(text); i++) {
					
					//
					Intent intent = new IntentRequestBuilder("http://facebook-pipes.appspot.com/").build();
					
					//Https request with parameters and specific handler
					//https://api.meetup.com/cities.xml/?state=ny&key=ABDE12456AB2324445
					//is it possible to send an array of parcelable as well
//					Map<String,String> parameters = new HashMap<String,String>();
//					parameters.put("key", "ABDE12456AB2324445");
//					parameters.put("state", "ny");
//
//					Intent intent = new IntentRequestBuilder("https://api.meetup.com/cities.xml/").withParams(parameters).
//						withHandlerKey(SimpleHttpService.CITIES_HANDLER).asPost().build();
					
					//Next
					//Post to http://api.meetup.com/ew/event/
					
					//Normal Http request
//					Intent intent = new RequestWriter("http://facebook-pipes.appspot.com/").attach(new ResultReceiver(new Handler()) {
//							@Override
//							protected void onReceiveResult(int resultCode, Bundle resultData) {
//								if(resultData == null) {
//									AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " but resultData is Null ");
//								} else {
//									AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " and result: " + resultData.getString(Request.SIMPLE_BUNDLE_RESULT));
//								}
//							}
//						}).write();
					
					//OLD WAY TO DO IT
					//is still possible to do it in this way
//					Intent intent = new Intent(HttpServiceConstant.request);
//					intent.putExtra(HttpServiceConstant.Extra.url, "http://facebook-pipes.appspot.com/");
//					intent.putExtra(HttpServiceConstant.Extra.request_parcable, new ResultReceiver(new Handler()) {
//						@Override
//						protected void onReceiveResult(int resultCode, Bundle resultData) {
//							if(resultData == null) {
//								AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " but resultData is Null ");
//							} else {
//								AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " and result: " + resultData.getString(Request.SIMPLE_BUNDLE_RESULT));
//							}
//						}
//					});
					
					startService(intent);
					start.setEnabled(true);
				}
			}
		});
		
		monitorInfo = ((TextView)findViewById(R.id.monitorInfo));
		monitorInfo.setText("Monitor is detach");
		
		start.setEnabled(false);
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppLogger.debug("Starting monitor");
				sendBroadcast(new Intent(SimpleHttpService.START_MONITOR_ACTION));
				start.setEnabled(false);
				stop.setEnabled(true);
				monitorInfo.setText("Attaching monitor...");
			}
		});
		stop.setEnabled(false);
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppLogger.debug("Stopping monitor");
				sendBroadcast(new Intent(SimpleHttpService.STOP_MONITOR_ACTION));
				start.setEnabled(true);
				stop.setEnabled(false);
				monitorInfo.setText("Monitor is detach");
			}
		});


		
//		log.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				AppLogger.debug("Changing log level");
//				String coreLevel = System.getProperty("log.tag.HttpService-Core");
//				if("VERBOSE".equals(coreLevel)) {
//					System.setProperty("log.tag.HttpService-Core", "WARN");
//					System.setProperty("log.tag.HttpService-Provider", "WARN");
//				} else {
//					System.setProperty("log.tag.HttpService-Core", "VERBOSE");
//					System.setProperty("log.tag.HttpService-Provider", "VERBOSE");
//				}
//			}
//		});
	}
	
	@Override
	protected void onResume() {
		registerReceiver(monitorBroadcastReceiver, SimpleHttpService.MONITOR_INTENT_FILTER);
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(monitorBroadcastReceiver);
		super.onPause();
	}
	
	private BroadcastReceiver monitorBroadcastReceiver =  new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ArrayList<String> keys = intent.getStringArrayListExtra(SimpleHttpService.DUMP_MONITOR_EXTRA);
			StringBuilder builder = new StringBuilder("Monitoring[ | ");
			StringBuilder viewBuilder = new StringBuilder();
			for (String key: keys) {
				builder.append(key).append(":").append(intent.getStringExtra(key)).append(" | ");
				viewBuilder.append(key).append(":").append(intent.getStringExtra(key)).append(" \n ");
			}
			AppLogger.debug(builder.append("]").toString());
			monitorInfo.setText(viewBuilder.toString());
		}
	};
	
}
