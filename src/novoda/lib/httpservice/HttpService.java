package novoda.lib.httpservice;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.executor.CallableWrapper;
import novoda.lib.httpservice.executor.ExecutorManager;
import novoda.lib.httpservice.executor.LifecycleHandler;
import novoda.lib.httpservice.executor.monitor.MonitorableExecutorService;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.http.HttpProvider;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.IntentRequestParser;
import novoda.lib.httpservice.request.Response;
import android.content.Intent;

/**
 * Responsibilities : transform an intent in a request
 * 
 * @author luigi@novoda.com
 *
 * @param <T>
 */
public abstract class HttpService extends MonitorableExecutorService {
	
	private Provider provider;
	
	public HttpService() {
		this(null, null, null, null);
	}

	public HttpService(Provider provider, EventBus eventBus, ExecutorManager executorManager, LifecycleHandler lifecycleHandler) {
		super(eventBus, executorManager, lifecycleHandler);
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
