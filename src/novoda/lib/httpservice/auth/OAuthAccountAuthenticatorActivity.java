
package novoda.lib.httpservice.auth;

import novoda.lib.httpservice.R;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class OAuthAccountAuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final int MSG_FETCH_TOKEN = 1;

    public static final int MSG_GET_AUTHORIZE_URL = 0;

    private volatile Looper mServiceLooper;

    private volatile ServiceHandler mServiceHandler;

    private AndroidHttpClient client = AndroidHttpClient.newInstance("Android");

    private String mName;

    private OAuthProvider provider;

    private OAuthConsumer consumer;

    private String callback;

    private static final String TAG = "OAuth";

    private String authorizationURLWithToken;

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final int what = msg.what;
            try {
                switch (what) {
                    case MSG_GET_AUTHORIZE_URL:
                        authorizationURLWithToken = provider.retrieveRequestToken(consumer,
                                callback);

                        // we have a Url
                        if (authorizationURLWithToken.startsWith("http")) {
                            oauthWebkitHack = getHack();
                        } else {
                            // throw Exception??
                        }

                        Log.i(TAG, authorizationURLWithToken);
                        break;
                    case MSG_FETCH_TOKEN:
                        final String remoteCallback = (String) msg.obj;
                        provider.retrieveAccessToken(consumer, Uri.parse(remoteCallback)
                                .getQueryParameter("oauth_verifier"));
                        consumer.getToken();
                        consumer.getTokenSecret();
                        onFinishLogin();
                        Log.i(TAG, consumer.getToken() + " " + consumer.getTokenSecret());
                        break;
                }
            } catch (Exception e) {
                Log.i(TAG, "error", e);
                setResponseFromException(e);
                finish();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "onNewIntent with: " + intent.toString());
        }
        if (intent != null && intent.getDataString() != null) {
            Message msg = Message.obtain(mServiceHandler);
            msg.what = MSG_FETCH_TOKEN;
            msg.obj = intent.getDataString();
            mServiceHandler.sendMessage(msg);
        }
    };

    private void setResponseFromException(Exception e) {
        response.onError(AccountManager.ERROR_CODE_NETWORK_ERROR, "test");
        Bundle d = new Bundle();
        d.putString(AccountManager.KEY_AUTH_FAILED_MESSAGE, "tests");
        d.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        d.putInt(AccountManager.KEY_ERROR_CODE, AccountManager.ERROR_CODE_NETWORK_ERROR);
        d.putParcelable(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        setAccountAuthenticatorResult(d);

        setAccountAuthenticatorResult(getIntent().getExtras());
        setResult(RESULT_CANCELED, getIntent());
    }

    /**
     * Called when response is received from the server for authentication
     * request. See onAuthenticationResult(). Sets the
     * AccountAuthenticatorResult which is sent back to the caller. Also sets
     * the authToken in AccountManager for this account.
     * 
     * @param the confirmCredentials result.
     */

    protected void onFinishLogin() {
        Log.i(TAG, "finishLogin()");
        final String authToken = consumer.getToken();
        final String authTokenSecret = consumer.getTokenSecret();
        final Account account = new Account(username.getText().toString(),
                "novoda.lib.httpservice.oauth");

        accountManager.addAccountExplicitly(account, authTokenSecret, null);
        final Intent intent = new Intent();

        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username.getText().toString());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "novoda.lib.httpservice.oauth");
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);

        accountManager.setUserData(account, "token", authToken);
        accountManager.setUserData(account, "tokenSecret", authTokenSecret);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    EditText mUsername;

    EditText mPassword;

    Button mLoginButton;

    private AccountAuthenticatorResponse response;

    private EditText username;

    private EditText password;

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        accountManager = AccountManager.get(this);
        CookieSyncManager.createInstance(getApplicationContext());
        setContentView(R.layout.lib_httpservice_login_preference);
        initializeViews();

        final Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("oauthMetaData")) {
            throw new IllegalStateException("can not start authentication without OAuth data");
        }

        response = intent.getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        OAuthMetaData data = intent.getParcelableExtra("oauthMetaData");
        callback = data.callback;

        consumer = new CommonsHttpOAuthConsumer(data.consumerKey, data.consumerSecret);
        provider = new CommonsHttpOAuthProvider(data.requestTokenEndpointUrl,
                data.accessTokenEndpointUrl, data.authorizationWebsiteUrl, client);

        HandlerThread thread = new HandlerThread("OAuthAccountAuthenticatorActivity[" + mName + "]");
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mServiceHandler.sendEmptyMessage(MSG_GET_AUTHORIZE_URL);
    }

    @Override
    protected void onResume() {
        CookieSyncManager.getInstance().startSync();
        super.onResume();
    }

    @Override
    protected void onPause() {
        CookieSyncManager.getInstance().stopSync();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
        client.close();
        super.onDestroy();
    }

    private void initializeViews() {
        username = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button submit = (Button) findViewById(R.id.create_new);
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                oauthWebkitHack.init(getApplicationContext());
                oauthWebkitHack.login(username.getText().toString().trim(), password.getText()
                        .toString().trim());
            }
        });
    }

    private OAuthLoginWebHack oauthWebkitHack;

    private OAuthLoginWebHack getHack() {
        return new OAuthLoginWebHack() {

            @Override
            protected String getPasswordDOMID() {
                return "password-oauthAuthorizeForm";
            }

            @Override
            protected String getOAuthLoginUrl() {
                return authorizationURLWithToken;
            }

            @Override
            protected String getLoginDOMID() {
                return "email-oauthAuthorizeForm";
            }

            @Override
            protected void onPreSubmit(WebView webkit) {
                onWebHackPreSubmit(webkit);
                super.onPreSubmit(webkit);
            }

            @Override
            protected void onCallback(String url) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "onCallBack: " + url);
                }
                if (url.startsWith(callback)) {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(url));
                    onNewIntent(intent);
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationURLWithToken));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            };
        };
    }

    protected void onWebHackPreSubmit(WebView webkit) {
    }

}
