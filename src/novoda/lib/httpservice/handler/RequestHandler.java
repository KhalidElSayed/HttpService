package novoda.lib.httpservice.handler;

import android.net.Uri;
import novoda.lib.httpservice.request.Response;

public interface RequestHandler {
	
	boolean match(Uri uri);

	void onStatusReceived(String status);

    void onHeadersReceived(String headers);

    void onThrowable(Throwable t);

	void onContentReceived(Response response);
	
}
