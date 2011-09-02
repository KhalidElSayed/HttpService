package com.novoda.httpservice.handler;

public interface HasHandlers {
	
	void remove(RequestHandler handler);

	void add(RequestHandler handler);
	
}
