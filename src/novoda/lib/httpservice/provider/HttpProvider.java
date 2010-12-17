package novoda.lib.httpservice.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpProvider<T> implements Provider<T> {

	private static final String USER_AGENT = new UserAgent.Builder().with("HttpService").build();

	private static final String ENCODING = "UTF-8";
    
    private HttpClient client;
    
	public HttpProvider() {
		client = AndroidHttpClient.newInstance(USER_AGENT);
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
			Writer writer = new StringWriter(8192);
			char[] buffer = new char[8192];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING), 8192);
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
