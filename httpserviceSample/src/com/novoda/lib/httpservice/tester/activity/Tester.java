package com.novoda.lib.httpservice.tester.activity;

import static com.novoda.lib.httpservice.tester.util.Log.v;

import java.util.ArrayList;

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

import com.novoda.lib.httpservice.tester.R;
import com.novoda.lib.httpservice.tester.service.SimpleHttpService;
import com.novoda.lib.httpservice.util.IntentBuilder;

public class Tester extends BaseActivity {

	/**
	 * Just an site that I owned and that I can call without getting in trouble for dos
	 */
	private static final String HOST = "http://httpmock.appspot.com/test/success";
	
	private TextView monitorInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		final EditText edit = ((EditText) findViewById(R.id.requestNumber));
//d		final Button call = ((Button)findViewById(R.id.start));
		final Button start = ((Button)findViewById(R.id.start));
		final Button startMonitor = ((Button)findViewById(R.id.startMonitor));
		final Button stop = ((Button)findViewById(R.id.stopMonitor));
		edit.setText("1");
//		call.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String text = edit.getText().toString();
//				d("Making " + text + " calls");
//				for(int i= 0; i<Integer.valueOf(text); i++) {
//					Intent intent = new IntentBuilder(SimpleHttpService.ACTION_REQUEST , HOST)
//						.withParam("param", "" + i)
//						.withConsumedResultReceiver(new ResultReceiver(new Handler()) {
//							@Override
//							protected void onReceiveResult(int resultCode, Bundle resultData) {
//								d(">> Service has finished to handle the result");
//							}
//						}).build();
//					startService(intent);
//					start.setEnabled(true);
//				}
//			}
//		});
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
//				d("Making all the same calls");
				for(int i= 0; i<Integer.valueOf(text); i++) {
					Intent intent = new IntentBuilder(SimpleHttpService.ACTION_REQUEST, HOST)
						.withStringResultReceiver(new ResultReceiver(new Handler()) {
							@Override
							protected void onReceiveResult(int resultCode, Bundle resultData) {
								v(">> SUCCESS");
							}
						}). withDisableCache().build();
					startService(intent);
					startMonitor.setEnabled(true);
				}
			}
		});
		
		monitorInfo = ((TextView)findViewById(R.id.monitorInfo));
		monitorInfo.setText("Monitor is detach");
		
		startMonitor.setEnabled(false);
		startMonitor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v("Starting monitor");
				sendBroadcast(new Intent(SimpleHttpService.ACTION_START_MONITOR));
				startMonitor.setEnabled(false);
				stop.setEnabled(true);
				monitorInfo.setText("Attaching monitor...");
			}
		});
		stop.setEnabled(false);
		stop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v("Stopping monitor");
				sendBroadcast(new Intent(SimpleHttpService.ACTION_STOP_MONITOR));
				startMonitor.setEnabled(true);
				stop.setEnabled(false);
				monitorInfo.setText("Monitor is detach");
			}
		});
	}
	
	@Override
	protected void onResume() {
		registerReceiver(monitorBroadcastReceiver, SimpleHttpService.INTENT_FILTER_MONITOR);
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
			v(builder.append("]").toString());
			monitorInfo.setText(viewBuilder.toString());
		}
	};
	
}
