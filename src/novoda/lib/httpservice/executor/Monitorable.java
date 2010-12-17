package novoda.lib.httpservice.executor;


public interface Monitorable {
	
	void attach(Monitor monitor);
	
	void startMonitoring();
	
	void stopMonitoring();

}
