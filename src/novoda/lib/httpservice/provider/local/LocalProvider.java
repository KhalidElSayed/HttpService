package novoda.lib.httpservice.provider.local;

import static novoda.lib.httpservice.util.LogTag.Provider.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Provider.debug;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.ProviderException;
import java.util.HashMap;

import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.Provider;
import novoda.lib.httpservice.request.Request;

public class LocalProvider implements Provider {
	
	public HashMap<String, String> map = new HashMap<String, String>();
	
	private EventBus eventBus;
	
	public LocalProvider(EventBus eventBus) {	
		if(eventBus == null) {
			throw new ProviderException("EventBus is null, can't procede");
		}
		this.eventBus = eventBus;
	}
	
	public LocalProvider(EventBus eventBus, String url, String content) {
		this(eventBus);
		map.put(url, content);
	}
	
	public void add(String url, String content) {
		map.put(url, content);
	}
	
	public InputStream getContent(String url) {
		if(map.containsKey(url)) {			
			return new ByteArrayInputStream(map.get(url).getBytes());
		} else {
			if(debugIsEnable()) {
				debug("There is no resource registered for the local provider for url : " + url);
			}
			return null;
		}
	}
	
	@Override
	public void execute(Request req) {
		InputStream content = getContent(req.getUrl());
		if(content == null) {
			eventBus.fireOnThrowable(req, new Throwable("Content not found"));
		}
		eventBus.fireOnContentReceived(req, content);
	}

}
