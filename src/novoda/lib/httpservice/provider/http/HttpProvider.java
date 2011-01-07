package novoda.lib.httpservice.provider.http;

import static novoda.lib.httpservice.util.HttpServiceLog.Provider.d;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.debugIsEnable;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.e;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.errorIsEnable;
import novoda.lib.httpservice.exception.ProviderException;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class HttpProvider implements Provider {

	private static final String USER_AGENT = new UserAgent.Builder().with("HttpService").build();
    
    private HttpClient client;
    
    private EventBus eventBus;

    public HttpProvider(EventBus eventBus) {		
		this(AndroidHttpClient.newInstance(USER_AGENT), eventBus);
	}
    
    public HttpProvider(HttpClient client, EventBus eventBus) {		
    	if(eventBus == null) {
    		logAndThrow("EventBus is null, can't procede");
    	}
		this.eventBus = eventBus;
		this.client = client;
	}

	@Override
	public Response execute(Request request) {
		Response response = new Response();
        HttpUriRequest method = null;
        try {
        	if(debugIsEnable()) {
    			d("HttpProvider execute for : " + request.getUri());
    		}
        	if(request.isGet()) {
        		method = new HttpGet(Request.asURI(request));
        	} else if(request.isPost()) {
        		method = new HttpPost(Request.asURI(request));
        	} else {
        		logAndThrow("Method " + request.getMethod() + " is not implemented yet");
        	}
        	HttpContext context = new BasicHttpContext();
        	eventBus.fireOnPreProcessRequest(request, method, context);
        	final HttpResponse httpResponse = client.execute(method, context);
        	eventBus.fireOnPostProcessRequest(request, httpResponse, context);
            if(httpResponse == null) {
            	logAndThrow("Response from " + request.getUri() + " is null");
            }
            response.setHttpResponse(httpResponse);
            response.setRequest(request);
            if(debugIsEnable()) {
    			d("Request returning response");
    		}
            return response;
        } catch (Throwable t) {
        	eventBus.fireOnThrowable(request, t);
        	if(errorIsEnable()) {
    			e("Problems executing the request for : " + request.getUri(), t);
    		}
    		throw new ProviderException("Problems executing the request for : " + request.getUri());
        }
	}
	
	private void logAndThrow(String msg) {
		if(errorIsEnable()) {
			e(msg);
		}
		throw new ProviderException(msg);
	}
	
}