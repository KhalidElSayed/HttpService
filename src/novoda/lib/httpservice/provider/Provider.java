package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.Request;
import novoda.lib.httpservice.handler.AsyncHandler;

public interface Provider<T> {

	void execute(Request request, AsyncHandler<T> asyncHandler);
	
}
