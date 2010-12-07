
package novoda.lib.httpservice.auth;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.res.XmlResourceParser;
import android.os.IBinder;
import android.util.Log;

public class OAuthAccountAuthenticatorService extends Service {

    private static final String TAG = "OAuth";

    private static OAuthAuthenticator sAccountAuthenticator = null;

    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "OAuthAccountAuthenticatorService Service started.");
        }
        
        final ComponentName name = new ComponentName(getPackageName(), getClass().getName());
        try {
            ServiceInfo serviceInfo = getPackageManager().getServiceInfo(name,
                    PackageManager.GET_META_DATA);
            XmlResourceParser xml = getResources().getXml(
                    serviceInfo.metaData.getInt("novoda.lib.httpservice.OAuthInformation"));
            OAuthMetaData m = OAuthMetaData.fromXml(xml);
            sAccountAuthenticator = new OAuthAuthenticator(this, m);
        } catch (Exception e) {
            throw new IllegalStateException(e.getCause());
        }
    }

    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent);
        }
        return sAccountAuthenticator.getIBinder();
    }

    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "OAuthAccountAuthenticatorService Authentication Service stopped.");
        }
    }

}
