
package novoda.lib.httpservice;

import org.apache.http.client.HttpClient;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ning.http.client.AsyncHttpProvider;

public class TestActivity extends Activity {

    private AsyncHttpProvider<HttpClient> apacheAsyncHttpProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AccountManager manager = AccountManager.get(this);
        for (Account account : manager.getAccountsByType("novoda.lib.httpservice.oauth")) {
            Log.i("TEST", account.name);
            Log.i("TEST", manager.getUserData(account, "token"));
            Log.i("TEST", manager.getUserData(account, "tokenSecret"));
            Log.i("TEST", manager.getPassword(account));
        }
        
        
//        java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
//        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
//
//        Builder config = new AsyncHttpClientConfig.Builder();
//        config.setResumableDownload(false);
//        AsyncHttpProvider p = new NettyAsyncHttpProvider(config.build());
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(p);
//        Future<String> f;
//        for (int i = 0; i < 100; i++) {
//            try {
//                f = asyncHttpClient.prepareGet("http://www.ning.com").execute(
//                        new AsyncHandler<String>() {
//
//                            @Override
//                            public void onThrowable(Throwable t) {
//                                // TODO Auto-generated method stub
//
//                            }
//
//                            @Override
//                            public com.ning.http.client.AsyncHandler.STATE onBodyPartReceived(
//                                    HttpResponseBodyPart bodyPart) throws Exception {
//                                // TODO Auto-generated method stub
//                                return null;
//                            }
//
//                            @Override
//                            public com.ning.http.client.AsyncHandler.STATE onStatusReceived(
//                                    HttpResponseStatus responseStatus) throws Exception {
//                                // TODO Auto-generated method stub
//                                return null;
//                            }
//
//                            @Override
//                            public com.ning.http.client.AsyncHandler.STATE onHeadersReceived(
//                                    HttpResponseHeaders headers) throws Exception {
//                                // TODO Auto-generated method stub
//                                return null;
//                            }
//
//                            @Override
//                            public String onCompleted() throws Exception {
//                                // TODO Auto-generated method stub
//                                return null;
//                            }
//                        });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        super.onCreate(savedInstanceState);
        
//        AsyncHttpClientConfig.Builder config = new AsyncHttpClientConfig.Builder();
//        NettyAsyncHttpProvider provider = new NettyAsyncHttpProvider(config.build());
//        
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(provider);
//        Future<String> f;
//        try {
//            f = asyncHttpClient.prepareGet("http://www.ning.com/").execute(new AsyncCompletionHandler<String>(){
//                @Override
//                public String onCompleted(Response response) throws Exception{
//                    return response.getResponseBody();
//                }
//                @Override
//                public void onThrowable(Throwable t){
//                }
//            });
//            String statuѕCode = f.get();
//            Log.i("TEST", statuѕCode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
    }
}
