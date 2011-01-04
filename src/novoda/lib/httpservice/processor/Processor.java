package novoda.lib.httpservice.processor;

import org.apache.http.protocol.HttpProcessor;

import android.net.Uri;

/**
 * A processor is the interface to mark objects that are able
 * to process request before and the response after the execution
 * of a http method.
 * 
 * @author luigi@novoda.com
 *
 */
public interface Processor extends HttpProcessor {

	/**
	 * The match method is used from the event bus
	 * to determine if a specific processor is suppose
	 * to intercept request and response.
	 * 
	 * @param uri
	 * @return
	 */
	boolean match(Uri uri);
	
}
