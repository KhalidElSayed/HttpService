package novoda.lib.httpservice;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.http.HttpProvider;
import novoda.lib.httpservice.request.IntentRequestParser;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.LifecycleManagedExecutorService;
import novoda.lib.httpservice.service.executor.CallableWrapper;
import novoda.lib.httpservice.service.executor.ExecutorManager;
import android.content.Intent;

/**
 * Responsibilities : transform an intent in a request, link together the request and the provider 
 * and give a clean class to extends to the users
 * 
 * It is depending on :<br>
 * - Provider : makes resources identified by uri available  (example http)
 * - EventBus : is responsible to deliver event to handlers
 * - ExecutorManager : controls the threads 
 * 
 * To intercept responsed you need to add a RequestHandler.
 * To do that is simple, implement a SimpleRequestHandler make sure to have implement at least
 * the match and the onContentReceived method.
 * 
 * Handler are very flexible. Just a simple example, if you use this body for the implementation 
 * of the match method you can intercept event made when the httpservice will handled that request.
 *  <code>
 *  if("/relative/path".equals(uri.getPath())) {
 *		return true;
 *	}
 *	return false;
 *	</code>
 * 
 * @author luigi@novoda.com
 */
public abstract class HttpService extends LifecycleManagedExecutorService {
	
	private Provider provider;
	
	public HttpService() {
		this(null, null, null);
	}

	/**
	 * Constructor with explicit dependencies.
	 * 
	 * @param provider Provider
	 * @param eventBus EventBus
	 * @param executorManager ExecutorManager
	 */
	public HttpService(Provider provider, EventBus eventBus, ExecutorManager executorManager) {
		super(eventBus, executorManager);
		if(provider == null) {
			this.provider = new HttpProvider(this.eventBus);
		} else {
			this.provider = provider;
		}
	}
	
	/**
	 * This method link the provider and the request. It is called by the
	 * ExecutorService to get a callable to execute.
	 */
	@Override
	public Callable<Response> getCallable(Intent intent) {
		if (debugIsEnable()) {
			d("Building up a callable with the provider and the request");
		}
		return new CallableWrapper(provider, buildRequest(intent));
	}
	
	/**
	 * If you want to change the way in witch the information  
	 * is passed from the intent to the service with the request
	 * you need to overwrite this method.
	 * 
	 * @param intent
	 * @return
	 */
	protected Request buildRequest(Intent intent) {
		return (Request)IntentRequestParser.parse(intent);
	}

}
