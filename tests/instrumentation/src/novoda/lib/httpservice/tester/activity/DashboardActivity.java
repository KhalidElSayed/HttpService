package novoda.lib.httpservice.tester.activity;

import novoda.lib.httpservice.HttpServiceConstant;
import novoda.lib.httpservice.tester.R;
import novoda.lib.httpservice.tester.service.SimpleHttpService;
import novoda.lib.httpservice.tester.util.AppLogger;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DashboardActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);
		final EditText edit = ((EditText) findViewById(R.id.requestNumber));
		edit.setText("1");
		
		final Button call = ((Button)findViewById(R.id.start));
		
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();
				AppLogger.debug("Making " + text + " calls");
				for(int i= 0; i<Integer.valueOf(text); i++) {
					Intent intent = new Intent(HttpServiceConstant.simple_request);
					intent.putExtra(HttpServiceConstant.Extra.url, "http://facebook-pipes.appspot.com/");
					startService(intent);
				}
			}
		});
		
		
		final Button start = ((Button)findViewById(R.id.startMonitor));
		final Button stop = ((Button)findViewById(R.id.stopMonitor));
		
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppLogger.debug("Starting monitor");
				sendBroadcast(new Intent(SimpleHttpService.START_MONITOR_ACTION));
				start.setEnabled(false);
				stop.setEnabled(true);
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
			}
		});
	}

	
}
