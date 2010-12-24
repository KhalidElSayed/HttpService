package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.LogTag.Provider.d;
import static novoda.lib.httpservice.util.LogTag.Provider.debugIsEnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.HasHandlers;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;
import android.os.Bundle;
import android.os.ResultReceiver;

public class EventBus implements HasHandlers {
	
	private static final String ENCODING = "UTF-8";
	
	public static final String DEFAULT_KEY = "default";
	
	private static final int BUFFER = 8192;
	
	public static final int SUCCESS = 200;
	
	public static final int ERROR = 500; 

	private static HashMap<String, List<GlobalHandler>> globalHandlers = new HashMap<String, List<GlobalHandler>>();
	
	private static HashMap<String, List<RequestHandler>> requestHandlers = new HashMap<String, List<RequestHandler>>();

	@Override
	public void addGlobalHandler(String key, GlobalHandler handler) {
		if(globalHandlers.containsKey(key)) {
			List<GlobalHandler> handlers = globalHandlers.get(key);
			List<GlobalHandler> newHandlers = new ArrayList<GlobalHandler>();
			newHandlers.addAll(handlers);
			newHandlers.add(handler);
			globalHandlers.put(key, newHandlers);
		} else {
			globalHandlers.put(key, Arrays.asList(handler));
		}
	}
	
	@Override
	public void addGlobalHandler(GlobalHandler handler) {
		addGlobalHandler(DEFAULT_KEY, handler);
	}
	
	@Override
	public void removeGlobalHandler(String key, GlobalHandler handler) {
		if(globalHandlers.containsKey(key)) {
			List<GlobalHandler> handlers = globalHandlers.get(key);
			List<GlobalHandler> newHandlers = new ArrayList<GlobalHandler>();
			newHandlers.addAll(handlers);
			newHandlers.remove(handler);
			globalHandlers.put(key, newHandlers);
		} else {
			return;
		}
	}
	
	@Override
	public void removeGlobalHandler(GlobalHandler handler) {
		removeGlobalHandler(DEFAULT_KEY, handler);
	}

	@Override
	public void addRequestHandler(String key, RequestHandler handler) {
		if(requestHandlers.containsKey(key)) {
			List<RequestHandler> handlers = requestHandlers.get(key);
			List<RequestHandler> newHandlers = new ArrayList<RequestHandler>();
			newHandlers.addAll(handlers);
			newHandlers.add(handler);
			requestHandlers.put(key, newHandlers);
		} else {
			requestHandlers.put(key, Arrays.asList(handler));
		}
	}
	
	@Override
	public void addRequestHandler(RequestHandler handler) {
		addRequestHandler(DEFAULT_KEY, handler);
	}

	@Override
	public void removeRequestHandler(String key, RequestHandler handler) {
		if(requestHandlers.containsKey(key)) {
			List<RequestHandler> handlers = requestHandlers.get(key);
			List<RequestHandler> newHandlers = new ArrayList<RequestHandler>();
			newHandlers.addAll(handlers);
			newHandlers.remove(handler);
			requestHandlers.put(key, newHandlers);
		} else {
			return;
		}
	}
	
	@Override
	public void removeRequestHandler(RequestHandler handler) {
		removeRequestHandler(DEFAULT_KEY, handler);
	}
	
    public void fireOnThrowable(Request request, Throwable t) {
    	if(debugIsEnable()) {
			d("Delivering content to handlers or receivers for onThrowable");
		}
    	if(request != null) {    		
    		ResultReceiver receiver = request.getResultReceiver();
    		if(receiver != null) {
    			receiver.send(ERROR, null);
    		} else {
    			if(debugIsEnable()) {
    				d("Receiver is null, not sending back the result");
    			}
    		}
    	}
    	
    	String key = getHandlerKey(request);
    	
    	if(globalHandlers.containsKey(key)) {
			List<GlobalHandler> handlers = globalHandlers.get(key);
			for(GlobalHandler handler : handlers) {
				handler.onThrowable(t);
			}
		}
		if(requestHandlers.containsKey(key)) {
			List<RequestHandler> handlers = requestHandlers.get(key);
			for(RequestHandler handler : handlers) {
				handler.onThrowable(t);
			}
		}
    }

	public void fireOnContentReceived(Response response) {
		Request request = response.getRequest();
		if(debugIsEnable()) {
			d("Delivering content to handlers or receivers for onContentReceiver");
		}
		if(request != null) {
			ResultReceiver receiver = request.getResultReceiver();
			if(receiver != null) {
				try {
					Bundle b = new Bundle();
					b.putString(Request.SIMPLE_BUNDLE_RESULT, convertStreamToString(response.getContent()));				
					receiver.send(SUCCESS, b);
				} catch(Throwable t) {
					receiver.send(ERROR, null);
				}
			} else {
				if(debugIsEnable()) {
					d("Receiver is null, not sending back the result");
				}
			}
		}
		
		String key = getHandlerKey(request);
		
		if(globalHandlers.containsKey(key)) {
			List<GlobalHandler> handlers = globalHandlers.get(key);
			for(GlobalHandler handler : handlers) {
				handler.onContentReceived(response);
			}
		}
		if(requestHandlers.containsKey(key)) {
			List<RequestHandler> handlers = requestHandlers.get(key);
			for(RequestHandler handler : handlers) {
				handler.onContentReceived(response);
			}
		}
	}
	
	private String getHandlerKey(Request request) {
		String key = null;
		if(request != null) {			
			key = request.getHandlerKey();
		}
    	if(key == null) {
    		key = DEFAULT_KEY;
    	}
    	return key;
	}
	
	private String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter(BUFFER);
			char[] buffer = new char[BUFFER];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING), BUFFER);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	
}
