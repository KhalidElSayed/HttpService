package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.request.Request;

public interface Provider<T> {

	void execute(Request request, AsyncHandler<T> asyncHandler);
	
}
