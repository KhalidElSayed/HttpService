package novoda.lib.httpservice;

import static novoda.lib.httpservice.util.LogTag.debugIsEnableForNS;
import static novoda.lib.httpservice.util.LogTag.debugNS;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.executor.monitor.MonitorableExecutorService;
import novoda.lib.httpservice.handler.CallableWrapper;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.http.HttpProvider;
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
public abstract class HttpService<T> extends MonitorableExecutorService<T> {
	
	private Provider provider;
	
	public HttpService() {
		this(null);
	}
	
	public HttpService(Provider provider) {
		if(provider == null) {
			this.provider = new HttpProvider();
		} else {
			this.provider = provider;
		}
	}
	
	@Override
	public Callable<T> getCallable(Intent intent) {
		if (debugIsEnableForNS()) {
			debugNS("Building up a callable with the provider and the request");
		}
		Request request = getRequest(intent);
		return new CallableWrapper<T>(provider, request);
	}
	
	protected Request getRequest(Intent intent) {
		return (Request)RequestBuilder.build(intent);
	}

}
