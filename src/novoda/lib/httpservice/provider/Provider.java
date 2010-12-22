package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.request.Request;

public interface Provider {

	void execute(Request request);
	
}
