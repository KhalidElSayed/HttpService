package novoda.lib.httpservice;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.executor.CallableWrapper;
import novoda.lib.httpservice.executor.monitor.MonitorableExecutorService;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.HasHandlers;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.http.HttpProvider;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.RequestReader;
import android.content.Intent;

/**
 * Responsibilities : transform an intent in a request
 * 
 * @author luigi@novoda.com
 *
 * @param <T>
 */
public abstract class HttpService<T> extends MonitorableExecutorService<T> implements HasHandlers {
	
	private Provider provider;
	
	private EventBus eventBus;
	
	public HttpService() {
		this(null, null);
	}
	
	public HttpService(EventBus eventBus, Provider provider) {
		if(provider == null) {
			this.eventBus = new EventBus();
			this.provider = new HttpProvider(this.eventBus);
		} else {
			this.eventBus = eventBus;
			this.provider = provider;
		}
	}
	
	@Override
	public Callable<T> getCallable(Intent intent) {
		if (debugIsEnable()) {
			d("Building up a callable with the provider and the request");
		}
		Request request = RequestReader.read(intent);
		return new CallableWrapper<T>(provider, request);
	}
	
	protected Request getRequest(Intent intent) {
		return (Request)RequestReader.read(intent);
	}

	@Override
	public void addGlobalHandler(String key, GlobalHandler handler) {
		eventBus.addGlobalHandler(key, handler);
	}
	
	@Override
	public void removeGlobalHandler(String key, GlobalHandler handler) {
		eventBus.removeGlobalHandler(key, handler);
	}

	@Override
	public void addRequestHandler(String key, RequestHandler handler) {
		eventBus.addRequestHandler(key, handler);
	}

	@Override
	public void removeRequestHandler(String key, RequestHandler handler) {
		eventBus.removeRequestHandler(key, handler);
	}

	@Override
	public void addGlobalHandler(GlobalHandler handler) {
		eventBus.addGlobalHandler(handler);
	}
	
	@Override
	public void removeGlobalHandler(GlobalHandler handler) {
		eventBus.removeGlobalHandler(handler);
	}

	@Override
	public void addRequestHandler(RequestHandler handler) {
		eventBus.addRequestHandler(handler);
	}

	@Override
	public void removeRequestHandler(RequestHandler handler) {
		eventBus.removeRequestHandler(handler);
	}
}
