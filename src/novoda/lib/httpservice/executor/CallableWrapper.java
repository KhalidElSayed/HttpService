package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.Core.debug;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.exception.HandlerException;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

public class CallableWrapper<T> implements Callable<T> {
	
	private Provider provider;
	
	private Request request;
	
	public CallableWrapper(Provider provider, Request request) {
		if(provider == null) {
			throw new HandlerException("Configuration problem! A Provider must be specified!");
		}
		this.provider = provider;
		if(request == null) {
			throw new HandlerException("Configuration problem! A request must be specified!");
		}
		this.request = request;
	}
	
	@Override
	public T call() throws Exception {
		if(debugIsEnable()) {
			debug("Executing request : " + request);
		}
		provider.execute(request);
		//don't care bout returning result, I'm sending through the handler in an async way
		return null;
	}

}
