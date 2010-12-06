
package novoda.lib.httpservice;

import static org.junit.Assert.assertEquals;

import com.xtremelabs.robolectric.RobolectricTestRunner;

import novoda.lib.httpservice.net.AsyncHttpClient;
import novoda.rest.concurrent.RequestHandler;
import novoda.rest.concurrent.RequestHandlerBase;

import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Future;

@RunWith(RobolectricTestRunner.class)
public class AsyncHttpClientTest {

    @Mock
    HttpClient client;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
        
        
    
    @Test
    public void testRequestHandler() throws Exception {
        when(client.execute((HttpUriRequest) anyObject(), (ResponseHandler) anyObject()))
                .thenAnswer(new Answer<String>() {

                    @Override
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        // TODO Auto-generated method stubg
                        return "testing t";
                    }
                });
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
        assertEquals("test", future.get());
    }
}
