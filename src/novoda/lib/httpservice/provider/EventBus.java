package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.LogTag.Provider.d;
import static novoda.lib.httpservice.util.LogTag.Provider.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Provider.w;
import static novoda.lib.httpservice.util.LogTag.Provider.warnIsEnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

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
	
	private List<RequestHandler> handlers = new ArrayList<RequestHandler>();

	@Override
	public void add(RequestHandler handler) {
		if(handler == null) {
			if(warnIsEnable()) {
				w("The handler is null, there is no point in adding it!");
			}
			return;
		}
		if(handlers.contains(handler)) {
			if(warnIsEnable()) {
				w("The handler is already registered!");
			}
			return;
		}
		handlers.add(handler);
	}
	
	@Override
	public void remove(RequestHandler handler) {
		if(handler == null) {
			if(warnIsEnable()) {
				w("The handler is null, can't remove it!");
			}
			return;
		}
		if(handlers.contains(handler)) {
			handlers.remove(handler);
		}
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
    	
    	for(RequestHandler handler: handlers) {
    		if(handler.match(request.getUri())) {
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
		
		for(RequestHandler handler: handlers) {
    		if(handler.match(request.getUri())) {
    			handler.onContentReceived(response);
    		}
    	}
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
