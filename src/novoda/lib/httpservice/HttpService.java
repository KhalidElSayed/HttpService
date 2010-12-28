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
 * Responsibilities : transform an intent in a request
 * 
 * @author luigi@novoda.com
 *
 * @param <T>
 */
public abstract class HttpService extends LifecycleManagedExecutorService {
	
	private Provider provider;
	
	public HttpService() {
		this(null, null, null);
	}

	public HttpService(Provider provider, EventBus eventBus, ExecutorManager executorManager) {
		super(eventBus, executorManager);
		if(provider == null) {
			this.provider = new HttpProvider(this.eventBus);
		} else {
			this.provider = provider;
		}
	}
	
	@Override
	public Callable<Response> getCallable(Intent intent) {
		if (debugIsEnable()) {
			d("Building up a callable with the provider and the request");
		}
		Request request = IntentRequestParser.parse(intent);
		return new CallableWrapper(provider, request);
	}
	
	protected Request getRequest(Intent intent) {
		return (Request)IntentRequestParser.parse(intent);
	}

}
