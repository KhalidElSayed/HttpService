package novoda.lib.httpservice;

import android.net.Uri;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.request.Response;

/**
 * Empty implementation of RequestHandler always matching.
 * Nice to extends so that you can override only the necessary methods
 * 
 * @author luigi@novoda.com
 *
 */
public class SimpleRequestHandler implements RequestHandler {

	@Override
	public void onContentReceived(Response response) {
	}

	@Override
	public void onHeadersReceived(String headers) {
	}

	@Override
	public void onStatusReceived(String status) {
	}

	@Override
	public void onThrowable(Throwable t) {
	}

	@Override
	public boolean match(Uri uri) {
		return true;
	}

}
