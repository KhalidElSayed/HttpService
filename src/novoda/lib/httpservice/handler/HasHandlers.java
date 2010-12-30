package novoda.lib.httpservice.handler;

/**
 * Simple interface to mark object that are able to add and remove requestHandlers
 * 
 * @author luigi@novoda.com
 */
public interface HasHandlers {
	
	void remove(RequestHandler handler);

	void add(RequestHandler handler);
	
}
