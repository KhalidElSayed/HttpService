package novoda.lib.httpservice;

import static novoda.lib.httpservice.util.LogTag.debugIsEnableForNS;
import static novoda.lib.httpservice.util.LogTag.debugNS;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.executor.ExecutorService;
import novoda.lib.httpservice.executor.Monitorable;
import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.handler.CallableWrapper;
import novoda.lib.httpservice.provider.HttpProvider;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.RequestBuilder;
import android.content.Intent;

/**
 * Responsibilities : transform an intent in a request
 * 
 * @author luigi
 *
 * @param <T>
 */
public abstract class HttpQueuedService<T> extends ExecutorService<T> implements Monitorable {
	
	private Provider<T> provider;
	
	public HttpQueuedService() {
		this(null);
	}
	
	public HttpQueuedService(Provider<T> provider) {
		if(provider == null) {
			this.provider = new HttpProvider<T>();
		} else {
			this.provider = provider;
		}
	}
	
	@Override
	public Callable<T> getCallable(Intent intent) {
		if (debugIsEnableForNS()) {
			debugNS("trying to get the callable");
		}
		AsyncHandler<T> handler = getHandler();
		Request request = getRequest(intent);
		return new CallableWrapper<T>(handler, provider, request);
	}

	protected abstract AsyncHandler<T> getHandler();
	
	protected Request getRequest(Intent intent) {
		return RequestBuilder.build(intent);
	}

}
