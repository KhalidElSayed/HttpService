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

	/**
	 * Just an site that I owned and that I can call without getting in trouble for dos
	 */
	private static final String HOST = "http://facebook-pipes.appspot.com/";
	
	private TextView monitorInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		final EditText edit = ((EditText) findViewById(R.id.requestNumber));
		final Button call = ((Button)findViewById(R.id.start));
		final Button start = ((Button)findViewById(R.id.startMonitor));
		final Button stop = ((Button)findViewById(R.id.stopMonitor));
		edit.setText("1");
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
				AppLogger.debug("Making " + text + " calls");
				for(int i= 0; i<Integer.valueOf(text); i++) {
					startService(new IntentRequestBuilder(SimpleHttpService.ACTION_REQUEST, HOST).build());
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
				sendBroadcast(new Intent(SimpleHttpService.ACTION_START_MONITOR));
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
				sendBroadcast(new Intent(SimpleHttpService.ACTION_STOP_MONITOR));
				start.setEnabled(true);
				stop.setEnabled(false);
				monitorInfo.setText("Monitor is detach");
			}
		});
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
			ArrayList<String> keys = intent.getStringArrayListExtra(SimpleHttpService.EXTRA_DUMP_MONITOR);
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
