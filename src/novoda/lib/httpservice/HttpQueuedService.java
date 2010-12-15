package novoda.lib.httpservice;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.executor.ExecutorService;
import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.handler.CallableWrapper;
import novoda.lib.httpservice.provider.HttpProvider;
import novoda.lib.httpservice.provider.Provider;
import android.content.Intent;

/**
 * Responsibilities : transform an intent in a request
 * 
 * @author luigi
 *
 * @param <T>
 */
public abstract class HttpQueuedService<T> extends ExecutorService<T> {
	
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
		AsyncHandler<T> handler = getHandler(intent);
		Request request = getRequest(intent);
		return new CallableWrapper<T>(handler, provider, request);
	}

	protected abstract AsyncHandler<T> getHandler(Intent intent);
	
	protected abstract Request getRequest(Intent intent);

}
