
package novoda.rest.concurrent;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import java.io.IOException;
import java.io.InputStream;

public abstract class RequestHandlerBase<T> implements RequestHandler<T> {


    public RequestHandlerBase() {
    }
    
    @Override
    public T call() throws Exception {
        //final HttpResponse response = client.execute(request);
        return null;
    }
    
    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        return null;
    }
    
    public abstract T parse(InputStream in) throws ParseException, IOException;
}
