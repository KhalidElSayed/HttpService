package novoda.lib.httpservice.provider.http;

import static novoda.lib.httpservice.util.LogTag.debugIsEnableForNS;
import static novoda.lib.httpservice.util.LogTag.debugNS;
import static novoda.lib.httpservice.util.LogTag.errorIsEnableForNS;
import static novoda.lib.httpservice.util.LogTag.errorNS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.ProviderException;
import novoda.lib.httpservice.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.os.Bundle;
import android.os.ResultReceiver;

public class HttpProvider implements Provider {

	private static final String USER_AGENT = new UserAgent.Builder().with("HttpService").build();

	private static final String ENCODING = "UTF-8";
    
    private HttpClient client;
    
	public HttpProvider() {
		client = AndroidHttpClient.newInstance(USER_AGENT);
	}

	@Override
	public void execute(Request request) {
		ResultReceiver receiver = request.getResultReceiver();
        HttpUriRequest get = new HttpGet(request.getUrl());
        HttpResponse response;
        try {
            response = client.execute(get);
            if(response == null) {
            	longAndThrow("Response from " + request.getUrl() + " is null");
            }
            HttpEntity entity = response.getEntity();
            if(entity == null) {
            	longAndThrow("Response from " + request.getUrl() + " is null");
            }
            String contentClass = request.getContentClassSimpleName();
            if(debugIsEnableForNS()) {
				debugNS("Processing content of class : " + contentClass);
			}
            if (String.class.getSimpleName().equals(contentClass)) {
	            String content = convertStreamToString(entity.getContent());
	            if(receiver == null) {
	            	longAndThrow("RequestReceiver is not valid");
	            }
	            Bundle b = new Bundle();
				b.putString(Request.SIMPLE_BUNDLE_RESULT, (String)content);				
				if(debugIsEnableForNS()) {
					debugNS("seding bundle on the receiver");
				}
	            receiver.send(SUCCESS, b);
            } else {
            	longAndThrow("The support for content type " + contentClass + " is not implemented yet");
            }
        } catch (Exception e) {
        	longAndThrow("Problems executing the request for : " + request.getUrl(), e);
        } finally {
        	get.abort();
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
	
	
	private void longAndThrow(String msg) {
		if(errorIsEnableForNS()) {
			errorNS(msg);
		}
		throw new ProviderException(msg);
	}
	
	private void longAndThrow(String msg, Throwable e) {
		if(errorIsEnableForNS()) {
			errorNS(msg, e);
		}
		throw new ProviderException(msg);
	}

}
