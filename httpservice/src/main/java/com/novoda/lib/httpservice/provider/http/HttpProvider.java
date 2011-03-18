package com.novoda.lib.httpservice.provider.http;

import static com.novoda.lib.httpservice.utils.Log.Provider.e;
import static com.novoda.lib.httpservice.utils.Log.Provider.errorLoggingEnabled;
import static com.novoda.lib.httpservice.utils.Log.Provider.v;
import static com.novoda.lib.httpservice.utils.Log.Provider.verboseLoggingEnabled;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.exception.ProviderException;
import com.novoda.lib.httpservice.provider.Provider;
import com.novoda.lib.httpservice.utils.IntentReader;
import com.novoda.lib.httpservice.utils.Log;

public class HttpProvider implements Provider {

	private static final String ENCODING = "UTF-8";
	
	private static final String USER_AGENT = new UserAgent.Builder().with("HttpService").build();
    
    private HttpClient client;

    public HttpProvider() {		
		this(AndroidHttpClient.newInstance(USER_AGENT));
	}
    
    public HttpProvider(HttpClient client) {
		this.client = client;
	}
    
    //Due to multithread activity actor should be used carefully
    private Actor actor;

	@Override
	public void execute(Actor actor) {
		this.actor = actor;
        actor.onResume();
        IntentReader reader = new IntentReader(actor.getIntent());
        try {
        	if(verboseLoggingEnabled()) {
    			v("HttpProvider execute for : " + actor.getIntent());
    		}
        	HttpUriRequest method = null;
        	if(reader.isGet()) {
        		method = initGet(reader);
        	} else if(reader.isDelete()) {
            	method = initDelete(reader);
        	} else if(reader.isPost()) {
        		method = initPost(reader);
        	} else {
        		logAndThrow("Method " + reader.getMethod() + " is not implemented yet");
        	}
        	HttpContext context = new BasicHttpContext();
        	
        	actor.onPreprocess(method, context);
        	final HttpResponse httpResponse = client.execute(method, context);
        	actor.onPostprocess(httpResponse, context);
        	actor.onHeaderReceived(httpResponse);
        	checkResponse(actor, httpResponse);
        	
            if(httpResponse == null) {
            	logAndThrow("Response from " + reader.getUri() + " is null");
            }
            if(verboseLoggingEnabled()) {
    			v("Request finished");
    		}
        } catch (Throwable t) {
        	actor.onThrowable(t);
        	if(errorLoggingEnabled()) {
    			e("Problems executing the request for : " + reader.getUri() + " " , t);
    		}
        }
        actor.onPause();
        actor.onDestroy();
	}

	@Override
	public void onLowMemory() {
		if(actor != null) {
			try {
				actor.onLowMemory();
			} catch(Throwable t) {
				Log.v("Problem calling on low memory on the current actor");
			}
		}
	}
	
	private void checkResponse(Actor actor, HttpResponse httpResponse) {
		StatusLine status = httpResponse.getStatusLine();
		if(verboseLoggingEnabled()) {
			v("Checking response status : " + status.getStatusCode());
		}
		if(200 == status.getStatusCode() || 206 == status.getStatusCode()) {
			actor.onResponseReceived(httpResponse);			
		} else {
			if(actor.onResponseError(status.getStatusCode())) {
				actor.onResponseReceived(httpResponse);
			}
		}
	}

	private HttpUriRequest initGet(IntentReader reader) {
		HttpUriRequest method = new HttpGet(reader.asURI());
		//TODO
		return method;
	}
	
	private HttpUriRequest initPost(IntentReader reader) {
		HttpPost method = new HttpPost(reader.asURI());
		
		if(reader.hasBodyEntity()) {
			try {
				String data = reader.getBodyEntity();
				method.setEntity(new StringEntity(data, ENCODING));
			} catch (UnsupportedEncodingException e) {
				Log.e("Problem setting entity in the body", e);
			}
		}
		
		//TODO
		return method;
	}
	
	private HttpUriRequest initDelete(IntentReader reader) {
		HttpUriRequest method = new HttpDelete(reader.asURI());
		//TODO
		return method;
	}
	
//	private void checkMultipartParams(HttpPost post, IntentWrapper intent) {
//		String fileParamName = intent.getMultipartFileParamName();
//		FileBody fileBody = getFileBodyFromFile(intent.getMultipartFile(), fileParamName);
//
//		String uriParamName = intent.getMultipartUriParamName();
//		FileBody uriBody = getFileBodyFromUri(intent.getMultipartUri(), uriParamName);
//		
//		String extraPram = intent.getMultipartExtraParam();
//		StringBody stringBody = getStringBody(extraPram, intent.getMultipartExtraValue());
//		
//		if(stringBody != null || fileBody != null || uriBody != null) {
//			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
//			if(stringBody != null) {
//				entity.addPart(extraPram, stringBody);
//			}
//			if(uriBody != null) {
//				entity.addPart(uriParamName, uriBody);
//			}
//			if(fileBody != null) {
//				entity.addPart(fileParamName, fileBody);
//			}
//			post.setEntity(entity);
//		}
//	}
//	
//	private FileBody getFileBodyFromUri(String uri, String paramName) {
//		if(TextUtils.isEmpty(paramName) || TextUtils.isEmpty(uri)) {
//			return null;
//		}
//		File f = null;
//		try {
//			f = new File(new URI(uri));
//		} catch (URISyntaxException e) {
//			if(verboseLoggingEnabled()) {
//				v("file not found " + uri);
//			} 
//		}
//		return new FileBody(f, ENCODING);
//	}
//	
//	private FileBody getFileBodyFromFile(String file, String paramName) {
//		if(TextUtils.isEmpty(file) || TextUtils.isEmpty(paramName)) {
//			return null;
//		}
//		return new FileBody(new File(file), ENCODING);
//	}
//	
//	private StringBody getStringBody(String param, String value) {
//		if(TextUtils.isEmpty(param)) {
//			return null;
//		}
//		if(value == null) {
//			value = "";
//		}
//		StringBody body = null;
//		try  {
//			body = new StringBody(value);
//		} catch(Throwable t) {
//			v(t.getMessage());
//		}
//		return body;
//	}
	
	private void logAndThrow(String msg) {
		if(errorLoggingEnabled()) {
			e(msg);
		}
		throw new ProviderException(msg);
	}
	
}