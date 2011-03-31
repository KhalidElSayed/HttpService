
package com.novoda.lib.httpservice.receiver;

import com.novoda.lib.httpservice.utils.Log;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.ProgressBar;

public class ProgressableView extends ResultReceiver {

    private ProgressBar progressBar;

    public ProgressableView(Handler handler, ProgressBar progressBar) {
        super(handler);
        this.progressBar = progressBar;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (Log.verboseLoggingEnabled()) {
            Log.v("getting some data from Progress download: " + resultData);
        }
        Log.i("TEST" + resultData);
        progressBar.setProgress(80);
        super.onReceiveResult(resultCode, resultData);
    }
}
