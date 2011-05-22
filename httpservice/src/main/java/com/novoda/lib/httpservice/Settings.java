package com.novoda.lib.httpservice;


public interface Settings {
	
	int SECOND = 1000;
	
	int SOCKET_TIMEOUT = 20*SECOND;
    int CONNECTION_TIMEOUT = 20*SECOND;
    int CON_MANAGER_TIMEOUT = 20*SECOND;
    
	int CORE_POOL_SIZE = 5;	
	int MAXIMUM_POOL_SIZE = 5;
	int KEEP_ALIVE = 0;
	int CONNECTION_PER_ROUTE = 5;
	int MAX_TOTAL_CONNECTION = 5;

	int BLOCKING_QUEUE = 10000;
	
	int THREAD_PRIORITY = Thread.NORM_PRIORITY-1;

}
