package novoda.lib.httpservice.tester.activity;

import java.util.ArrayList;

import novoda.lib.httpservice.HttpServiceConstant;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.tester.R;
import novoda.lib.httpservice.tester.service.SimpleHttpService;
import novoda.lib.httpservice.tester.util.AppLogger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
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
		edit.setText("1");
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
				AppLogger.debug("Making " + text + " calls");
				for(int i= 0; i<Integer.valueOf(text); i++) {
					Intent intent = new Intent(HttpServiceConstant.simple_request);
					intent.putExtra(HttpServiceConstant.Extra.url, "http://facebook-pipes.appspot.com/");
					intent.putExtra(HttpServiceConstant.Extra.request_parcable, new ResultReceiver(new Handler()) {
						@Override
						protected void onReceiveResult(int resultCode, Bundle resultData) {
							if(resultData == null) {
								AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " but resultData is Null ");
							} else {
								AppLogger.logVisibly("onReceiveResult with status : " + resultCode + " and result: " + resultData.getString(Request.SIMPLE_BUNDLE_RESULT));
							}
						}
					});
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
