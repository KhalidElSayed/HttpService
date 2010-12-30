package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.request.Request;
import novoda.lib.httpservice.request.Response;

public interface Provider {

	Response execute(Request request);
	
}
