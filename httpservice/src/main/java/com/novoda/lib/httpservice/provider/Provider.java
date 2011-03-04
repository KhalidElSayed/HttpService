package com.novoda.lib.httpservice.provider;

import com.novoda.lib.httpservice.actor.Actor;

/**
 * Provider define the interface for every kind of
 * content provider. In 99% of the scenario the content is 
 * some result coming from an Http Request.
 * 
 * @author luigi
 *
 */
public interface Provider {
	
	int SECOND = 1000;
	int SOCKET_TIMEOUT = 25*SECOND;
    int CONNECTION_TIMEOUT = 25*SECOND;
    int CON_MANAGER_TIMEOUT = 25*SECOND;

	void execute(Actor actor);

	void onLowMemory();
	
}
