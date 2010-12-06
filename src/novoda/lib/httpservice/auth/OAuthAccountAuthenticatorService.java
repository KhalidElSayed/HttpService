
package novoda.lib.httpservice.auth;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.content.res.XmlResourceParser;
import android.os.IBinder;
import android.util.Log;

public class OAuthAccountAuthenticatorService extends Service {

    private static final String TAG = "OAuth";

    private static OAuthAuthenticator sAccountAuthenticator = null;

    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "OAuthAccountAuthenticatorService Authentication Service started.");
        }
        final ComponentName name = new ComponentName(getPackageName(), getClass().getName());
        try {
            ServiceInfo serviceInfo = getPackageManager().getServiceInfo(name,
                    PackageManager.GET_META_DATA);
            XmlResourceParser xml = getResources().getXml(
                    serviceInfo.metaData.getInt("novoda.lib.httpservice.OAuthInformation"));
            Log.i(TAG,
                    " met "
                            + serviceInfo.metaData
                                    .getInt("novoda.lib.httpservice.OAuthInformation"));

            OAuthMetaData m = OAuthMetaData.fromXml(xml);
            sAccountAuthenticator = new OAuthAuthenticator(this, m);

        } catch (NameNotFoundException e) {
            Log.e(TAG,
                    "You need to have a metadata tag attached to this service which contains oauth information");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
