package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.HttpServiceLog.Provider.d;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.debugIsEnable;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.w;
import static novoda.lib.httpservice.util.HttpServiceLog.Provider.warnIsEnable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import novoda.lib.httpservice.exception.RequestException;
import novoda.lib.httpservice.handler.HasHandlers;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.processor.HasProcessors;
import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.ResultReceiver;

/**
 * This class is responsible to register/unregister handlers and processors.
 * Plus has to deliver the events to handlers and processors.
 * <br>
 * It is clear that the double responsibility of managing processors and handlers
 * can be a sign to do some refactoring.
 * 
 * @author luigi@novoda.com
 *
 */
public class EventBus implements HasHandlers, HasProcessors {
	
	public static final String DEFAULT_KEY = "default";
	
	public static final int SUCCESS = 200;
	
	public static final int ERROR = 500; 
	
	private List<RequestHandler> handlers = new ArrayList<RequestHandler>();
	
	private List<Processor> processors = new ArrayList<Processor>();

	@Override
	public void add(RequestHandler handler) {
		add(handlers, handler);
	}
	
	@Override
	public void remove(RequestHandler handler) {
		remove(handlers, handler);
	}
	
	@Override
	public void add(Processor processor) {
		add(processors, processor);
	}

	@Override
	public void remove(Processor processor) {
		remove(processors, processor);
	}
	
	/**
	 * This method is called when there is an exception during the execution of a request.
	 * It is propagating onThrowable down to handlers.
	 * 
	 * @param request
	 * @param t
	 */
    public void fireOnThrowable(Request request, Throwable t) {
    	if(debugIsEnable()) {
			d("Delivering content to handlers or receivers for onThrowable");
		}
    	if(request != null) {    		
    		ResultReceiver receiver = request.getResultReceiver();
    		if(receiver != null) {
    			receiver.send(ERROR, null);
    		}
    	}
    	
    	for(RequestHandler handler: handlers) {
    		if(handler.match(request)) {
    			handler.onThrowable(t);
    		}
    	}
    }

    /**
	 * This method is called when the content of a request is received.
	 * It is propagating onContentReceived down to handlers.
	 * 
	 * @param request
	 */
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
					b.putString(Request.SIMPLE_BUNDLE_RESULT, getContentAsString(response.getHttpResponse()));
					receiver.send(SUCCESS, b);
				} catch(Throwable t) {
					receiver.send(ERROR, null);
				}
			}
		}
		
		for(RequestHandler handler: handlers) {
    		if(handler.match(request)) {
    			handler.onContentReceived(response);
    		}
    	}
	}
	
	/**
	 * This event is fired when the content has been consumed. 
	 * 
	 * @param request
	 */
	public void fireOnContentConsumed(Request request) {
		if(debugIsEnable()) {
            d("Firing onContentConsumed");
        }
		if(request != null) {
			ResultReceiver receiver = request.getResultConsumedReceiver();
			if(receiver != null) {
				try {
					receiver.send(SUCCESS, null);
				} catch(Throwable t) {
					receiver.send(ERROR, null);
				}
			}
		}
		
		for(RequestHandler handler: handlers) {
    		if(handler.match(request)) {
    			handler.onContentConsumed(request);
    		}
    	}
	}
	
	/**
	 * This event is fired before the execution of a request. In this way a processor
	 * can intercept the request before is made and execute some logic.
	 * 
	 * @param request
	 * @param request
	 * @param context
	 */
	public void fireOnPreProcessRequest(Request request, HttpRequest httpRequest, HttpContext context) {
	    if(debugIsEnable()) {
            d("pre process request");
        }
		for(Processor processor: processors) {
    		if(processor.match(request)) {
    			try {
					processor.process(httpRequest, context);
				} catch (Exception e) {
					throw new RequestException("Exception preprocessing content", e);
				}
    		}
    	}
	}
	
	/**
	 * This event is fired after the execution of a request. In this way a processor
	 * can intercept the request after is made and execute some logic on the response.
	 * 
	 * @param uri
	 * @param response
	 * @param context
	 */
	public void fireOnPostProcessRequest(Request request, HttpResponse response, HttpContext context) {
		for(ListIterator<Processor> iterator = processors.listIterator(processors.size()); iterator.hasPrevious();) {
			final Processor processor = iterator.previous();
			if(processor.match(request)) {
				try {
					processor.process(response, context);
				} catch (Exception e) {
					throw new RequestException("Exception preprocessing content", e);
				}
			}			
		}
	}
	
	private String getContentAsString(HttpResponse httpResponse) {
		HttpEntity entity = null;
		try {
			entity = httpResponse.getEntity();
			return EntityUtils.toString(entity);
		} catch (Exception e) {
			throw new RequestException("Exception converting entity to string", e);
		} finally {
			try {
				entity.consumeContent();
			} catch (Exception e) {
				throw new RequestException("Exception consuming content", e);
			}
		}
	}
	
	private static final <T> void remove(List<T> ts, T t) {
		if(t == null) {
			if(warnIsEnable()) {
				w("Trying to remove null object, can't remove it!");
			}
			return;
		}
		if(ts.contains(t)) {
			ts.remove(t);
		}
	}
	
	private static final <T> void add(List<T> ts, T t) {
		if(t == null) {
			if(warnIsEnable()) {
				w("The object is null, there is no point in adding it to the event bus!");
			}
			return;
		}
		if(ts.contains(t)) {
			if(warnIsEnable()) {
				w("The object is already registered!");
			}
			return;
		}
		ts.add(t);	
	}

}
