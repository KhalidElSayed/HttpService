package novoda.lib.httpservice.handler;


public interface HasHandlers {
	
	void addGlobalHandler(String key, GlobalHandler handler);
	
	void removeGlobalHandler(String key, GlobalHandler handler);
		
	void addRequestHandler(String key, RequestHandler handler);

	void removeRequestHandler(String key, RequestHandler handler);

	void addGlobalHandler(GlobalHandler handler);

	void removeGlobalHandler(GlobalHandler handler);

	void addRequestHandler(RequestHandler handler);

	void removeRequestHandler(RequestHandler handler); 
	
}
