package novoda.lib.httpservice.handler;

import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

public interface RequestHandler {
	
	boolean match(Request request);

	void onStatusReceived(String status);

    void onHeadersReceived(String headers);

    void onThrowable(Throwable t);

	void onContentReceived(Response response);
	
}
