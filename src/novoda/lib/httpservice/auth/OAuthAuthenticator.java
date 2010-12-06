
package novoda.lib.httpservice.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class OAuthAuthenticator extends AbstractAccountAuthenticator {

    private final Context mContext;

    private OAuthMetaData metaData;

    public OAuthAuthenticator(Context context, OAuthMetaData metaData) {
        super(context);
        this.mContext = context;
        this.metaData = metaData;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
            String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {
        Bundle result = new Bundle();
        Intent i = new Intent(mContext, OAuthAccountAuthenticatorActivity.class);
        i.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        result.putParcelable(AccountManager.KEY_INTENT, i);
        result.putParcelable("oauthMetaData", metaData);
        return result;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
            Bundle options) throws NetworkErrorException {
        Log.i("TEST", "=" + options.toString());

        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        Log.i("TEST", "= pro" + response.toString());

        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {
        Log.i("TEST", "=" + options.toString());

        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        Log.i("TEST", "=" + authTokenType.toString());

        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
            String[] features) throws NetworkErrorException {
        Log.i("TEST", "=" + account.toString());

        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {
        Log.i("TEST", "=" + options.toString());

        return null;
    }

}
