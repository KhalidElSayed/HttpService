package com.novoda.httpservice;

public class Settings {
	
	private static final int SECOND = 1000;

	private static final int SOCKET_TIMEOUT = 20*SECOND;
	private static final int CONNECTION_TIMEOUT = 20*SECOND;
	private static final int CON_MANAGER_TIMEOUT = 20*SECOND;
    
	private static int CORE_POOL_SIZE = 10;	
	private static int MAXIMUM_POOL_SIZE = 10;
	private static final int KEEP_ALIVE = 0;
	private static int CONNECTION_PER_ROUTE = 10;
	private static int MAX_TOTAL_CONNECTION = 10;

	private static int BLOCKING_QUEUE = 300;
	
	public int socketTimeout = SOCKET_TIMEOUT;
	public int connectionTimeout = CONNECTION_TIMEOUT;
	public int connectionManagerTimeout = CON_MANAGER_TIMEOUT;
	public int corePoolSize = CORE_POOL_SIZE;
	public int maximunPoolSize = MAXIMUM_POOL_SIZE;
	public int keepAlive = KEEP_ALIVE;
	public int connectionPerRoute = CONNECTION_PER_ROUTE;
	public int maxTotalConnection = MAX_TOTAL_CONNECTION;
	public int blockingQueue = BLOCKING_QUEUE;
	
	public void setConnectionNumber(int number) {
		connectionPerRoute = 20;
		corePoolSize = 20;
		maximunPoolSize = 20;
		maxTotalConnection = 20;
	}

}
