package novoda.lib.httpservice.provider;

import novoda.lib.httpservice.request.Request;

public interface Provider {
	
	public static final int SUCCESS = 200; 
	
	public static final int NOT_FOUND = 404;
	
	public static final int ERROR = 500; 

	void execute(Request request);
	
}
