
package novoda.lib.httpservice;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.setProperty("java.net.preferIPv6Addresses", "false");
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
