package novoda.lib.httpservice.handler;

import java.util.concurrent.Callable;

import novoda.lib.httpservice.Request;
import novoda.lib.httpservice.provider.Provider;

public class CallableWrapper<T> implements Callable<T> {

	private AsyncHandler<T> handler;
	
	private Provider<T> provider;
	
	private Request request;
	
	public CallableWrapper(AsyncHandler<T> handler, Provider<T> provider, Request request) {
		if(handler == null) {
			throw new HandlerException("Configuration problem! An Handler must be specified!");
		}
		this.handler = handler;
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
		provider.execute(request, handler);
		//don't care bout returning result, I'm sending through the handler in an async way
		return null;
	}

}
