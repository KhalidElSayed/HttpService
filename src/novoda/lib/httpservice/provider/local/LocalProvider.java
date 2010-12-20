package novoda.lib.httpservice.provider.local;

import java.util.HashMap;

import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.ProviderException;
import novoda.lib.httpservice.request.Request;

public class LocalProvider<T> implements Provider<T> {

	public HashMap<String, T> map = new HashMap<String, T>();
	
	public LocalProvider() {
	}
	
	public LocalProvider(String url, T content) {
		map.put(url, content);
	}
	
	public void add(String url, T content) {
		map.put(url, content);
	}
	
	public T getContent(String url) {
		if(map.containsKey(url)) {			
			return map.get(url);
		} else {
			throw new ProviderException("There is no resource registered for the local provider for url : " + url);
		}
	}
	
	@Override
	public void execute(Request req, AsyncHandler<T> asyncHandler) {
		asyncHandler.onContentReceived(getContent(req.getUrl()));
	}

}
