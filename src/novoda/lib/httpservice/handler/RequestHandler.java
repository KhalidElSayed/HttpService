package novoda.lib.httpservice.handler;

import java.io.InputStream;

public interface RequestHandler {

	void onStatusReceived(String status);

    void onHeadersReceived(String headers);

    void onThrowable(Throwable t);

	void onContentReceived(InputStream content);
	
}
