package novoda.lib.httpservice.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import novoda.lib.httpservice.Request;
import novoda.lib.httpservice.handler.AsyncHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpProvider<T> implements Provider<T> {

	private static final String HTTP = "http";

	private static final String ENCODING = "UTF-8";
	
	private static final int HTTP_PORT = 80;

    private static final int TIMEOUT_CONNECTION = 3000;

    private static final int TIMEOUT_SOCKET = 27000;
    
    private HttpClient client;
    
	public HttpProvider() {
		HttpParams params = new BasicHttpParams();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme(HTTP, PlainSocketFactory
				.getSocketFactory(), HTTP_PORT));
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);
		ConnManagerParams.setTimeout(params, TIMEOUT_CONNECTION);
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);
		client = new DefaultHttpClient(cm, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute(Request request, AsyncHandler<T> asyncHandler) {
        HttpUriRequest get = new HttpGet(request.getUrl());
        HttpResponse response;
        try {
            response = client.execute(get);
            if(response == null) {
            	throw new ProviderException("Response from " + request.getUrl() + " is null");
            }
            HttpEntity entity = response.getEntity();
            if(entity == null) {
            	throw new ProviderException("Response from " + request.getUrl() + " is null");
            }
            Class<?> clazz = asyncHandler.getContentClass();
            if (clazz == String.class) {
	            String content = convertStreamToString(entity.getContent());
	            if(asyncHandler == null) {
	            	throw new ProviderException("AsynchHnadler is not valid");
	            }
	            asyncHandler.onContentReceived((T)content);
            } else if(clazz == InputStream.class) {
	            asyncHandler.onContentReceived((T)entity.getContent());
            } else {
            	throw new ProviderException("The support for content type " + clazz + " is not implemented yet");
            }
        } catch (Exception e) {
            throw new ProviderException("Problems executing the request for : " + request.getUrl());
        }
	}

	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}

}
