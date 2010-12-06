
package novoda.lib.httpservice.auth;

import android.accounts.AccountAuthenticatorActivity;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OAuthAccountAuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final int MSG_FETCH_LOGIN_URL = 0;

    public static final int MSG_FETCH_TOKEN = 1;

    private volatile Looper mServiceLooper;

    private volatile ServiceHandler mServiceHandler;

    private static AndroidHttpClient client = AndroidHttpClient.newInstance("Android");

    private String mName;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            switch (what) {
                case MSG_FETCH_LOGIN_URL:
                    break;
                case MSG_FETCH_TOKEN:
                    break;
            }
        }
    }

    EditText mUsername;

    EditText mPassword;

    Button mLoginButton;
//
//    public OAuthAccountAuthenticatorActivity() {
//        super();
//        Log.i("TEST", "te");
//    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.i("TEST", icicle + " ");
        
        Log.i("TEST", " intent " + getIntent());
        HandlerThread thread = new HandlerThread("OAuthAccountAuthenticatorActivity[" + mName + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        Toast.makeText(this, "test", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
        client.close();
    }

}
