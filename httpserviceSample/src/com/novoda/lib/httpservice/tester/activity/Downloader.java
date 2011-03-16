package com.novoda.lib.httpservice.tester.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.novoda.lib.httpservice.R;
import com.novoda.lib.httpservice.tester.service.AppService;
import com.novoda.lib.httpservice.utils.IntentBuilder;

public class Downloader extends BaseActivity {

	private static final String HOST = "http://httpmock.appspot.com/test.zip";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloader);
		
		final Button start = ((Button)findViewById(R.id.download));
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentBuilder builder = new IntentBuilder(HOST, AppService.ZIP);
				startService(builder.build());
			}
		});
	}
	
}
