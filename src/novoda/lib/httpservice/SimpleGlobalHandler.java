package novoda.lib.httpservice;

import android.net.Uri;
import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.request.Response;

/**
 * Empty implementation of GlobalHandler always matching.
 * Nice to extends so that you can override only the necessary methods
 * 
 * @author luigi@novoda.com
 *
 */
public class SimpleGlobalHandler implements GlobalHandler {

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
	public void onProgress() {
	}

	@Override
	public boolean match(Uri uri) {
		return true;
	}

}
