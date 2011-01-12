package novoda.lib.httpservice.util;

import static novoda.lib.httpservice.util.Log.v;
import static novoda.lib.httpservice.util.Log.verboseLoggingEnabled;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Receiver that check the intent receiver and based on that
 * made a call to onConnectionLost and onConnectionResume. 
 * <br>
 * Use the static IntentFilter to register the receiver:<br>
 * - CONNECTIVITY_FILTER<br>
 * - SETTING_CHANGED_FILTER<br>
 * 
 * @author luigi@novoda.com
 *
 */
public abstract class ConnectivityReceiver extends BroadcastReceiver {

	public static final IntentFilter CONNECTIVITY_FILTER = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
	
    public static final IntentFilter SETTING_CHANGED_FILTER = new IntentFilter(ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED);
	
	@Override
	public void onReceive(Context context, Intent intent) {
		final String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true)) {
                onConnectionLost();
            }
            if (intent.hasExtra(ConnectivityManager.EXTRA_NETWORK_INFO)) {
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info.isConnectedOrConnecting()) {
                	if (verboseLoggingEnabled()) {
                        v("Connectivity is back, resuming for: " + info.toString());
                    }
                    onConnectionResume();
                }
            }
        }
	}
	
	protected abstract void onConnectionLost();
	
	protected abstract void onConnectionResume();

}
