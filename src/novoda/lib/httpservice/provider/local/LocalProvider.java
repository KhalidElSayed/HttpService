package novoda.lib.httpservice.provider.local;

import java.util.HashMap;

import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.provider.ProviderException;
import novoda.lib.httpservice.request.Request;
import android.os.Bundle;
import android.os.ResultReceiver;

public class LocalProvider implements Provider {
	
	public HashMap<String, Object> map = new HashMap<String, Object>();
	
	public LocalProvider() {
	}
	
	public LocalProvider(String url, Object content) {
		map.put(url, content);
	}
	
	public void add(String url, Object content) {
		map.put(url, content);
	}
	
	public Object getContent(String url) {
		if(map.containsKey(url)) {			
			return map.get(url);
		} else {
			throw new ProviderException("There is no resource registered for the local provider for url : " + url);
		}
	}
	
	@Override
	public void execute(Request req) {
		ResultReceiver receiver = req.getResultReceiver();
		if(receiver != null) {
			Object content = getContent(req.getUrl());
			if(content == null) {
				receiver.send(NOT_FOUND, null);	
			}
			Bundle b = new Bundle();
			if(String.class.getSimpleName().equals(req.getContentClassSimpleName())) {
				b.putString(Request.SIMPLE_BUNDLE_RESULT, (String)content);				
			} else {
				throw new ProviderException("The support for content type " + req.getContentClassSimpleName() + " is not implemented yet");
			}
			receiver.send(SUCCESS, b);
		}
	}

}
