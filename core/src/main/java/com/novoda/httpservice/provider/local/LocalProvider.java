package com.novoda.httpservice.provider.local;

import static com.novoda.httpservice.util.Log.Provider.v;
import static com.novoda.httpservice.util.Log.Provider.verboseLoggingEnabled;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import com.novoda.httpservice.exception.ProviderException;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.provider.Provider;
import com.novoda.httpservice.provider.Response;
import android.net.Uri;

public class LocalProvider implements Provider {
	
	public HashMap<Uri, String> map = new HashMap<Uri, String>();
	
	private EventBus eventBus;
	
	public LocalProvider(EventBus eventBus) {	
		if(eventBus == null) {
			throw new ProviderException("EventBus is null, can't procede");
		}
		this.eventBus = eventBus;
	}
	
	public LocalProvider(EventBus eventBus, Uri uri, String content) {
		this(eventBus);
		map.put(uri, content);
	}
	
	public void add(Uri uri, String content) {
		map.put(uri, content);
	}
	
	public void add(String url, String content) {
		map.put(Uri.parse(url), content);
	}
	
	public InputStream getContent(Uri uri) {
		if(map.containsKey(uri)) {			
			return new ByteArrayInputStream(map.get(uri).getBytes());
		} else {
			if(verboseLoggingEnabled()) {
				v("There is no resource registered for the local provider for url : " + uri);
			}
			return null;
		}
	}
	
	@Override
	public Response execute(IntentWrapper req) {
		InputStream content = getContent(req.getUri());
		if(content == null) {
			eventBus.fireOnThrowable(req, new Throwable("Content not found"));
			throw new ProviderException("Content not found");
		}
		Response response = new Response();
		response.setIntentWrapper(req);
		response.setContent(content);
		eventBus.fireOnContentReceived(response);
		return response;
	}

}
