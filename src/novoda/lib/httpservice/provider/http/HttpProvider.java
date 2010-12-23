package novoda.lib.httpservice.provider.http;

import static novoda.lib.httpservice.util.LogTag.Provider.debug;
import static novoda.lib.httpservice.util.LogTag.Provider.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Provider.error;
import static novoda.lib.httpservice.util.LogTag.Provider.errorIsEnable;
import novoda.lib.httpservice.exception.ProviderException;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;

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
	public void execute(Request request) {
        HttpUriRequest method = null;
        try {
        	if(debugIsEnable()) {
    			debug("HttpProvider execute for : " + request.getUri());
    		}
        	if(request.isGet()) {
        		method = new HttpGet(Request.asURI(request));
        	} else if(request.isPost()) {
        		method = new HttpPost(Request.asURI(request));
        	} else {
        		logAndThrow("Method " + request.getMethod() + " is not implemented yet");
        	}
        	final HttpResponse response = client.execute(method);
            if(response == null) {
            	logAndThrow("Response from " + request.getUri() + " is null");
            }
            final HttpEntity entity = response.getEntity();
            if(entity == null) {
            	logAndThrow("Entity from response of " + request.getUri() + " is null");
            }
            eventBus.fireOnContentReceived(request, entity.getContent());
        } catch (Throwable t) {
        	eventBus.fireOnThrowable(request, t);
        	logAndThrow("Problems executing the request for : " + request.getUri(), t);
        } finally {
        	if(method != null) {
        		method.abort();
        	}
        }
	}
	
	private void logAndThrow(String msg) {
		if(errorIsEnable()) {
			error(msg);
		}
		throw new ProviderException(msg);
	}
	
	private void logAndThrow(String msg, Throwable e) {
		if(errorIsEnable()) {
			error(msg, e);
		}
		throw new ProviderException(msg);
	}
	
}
