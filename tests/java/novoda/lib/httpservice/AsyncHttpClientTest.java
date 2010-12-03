
package novoda.lib.httpservice;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import novoda.lib.httpservice.net.AsyncHttpClient;
import novoda.rest.concurrent.RequestHandler;
import novoda.rest.concurrent.RequestHandlerBase;

import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

@RunWith(RobolectricTestRunner.class)
public class AsyncHttpClientTest {

    @Mock
    HttpClient client;

    @Test
    public void testRequestHandler() throws Exception {
        AsyncHttpClient aclient = new AsyncHttpClient(client);
        HttpUriRequest request = new HttpGet("http://google.com");
        RequestHandler<String> handler = new RequestHandlerBase<String>() {

            @Override
            public void onThrowable(Throwable e) {
            }

            @Override
            public void onTimeout() {
            }

            @Override
            public void onPreCall(HttpUriRequest request) {
            }

            @Override
            public void onPostCall(String data) {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onUnauthorized(int httpCode) {
            }

            @Override
            public String parse(InputStream in) throws ParseException, IOException {
                return "test";
            }
            
        };
        Future<String> future = aclient.execute(request, handler);
    }
}
