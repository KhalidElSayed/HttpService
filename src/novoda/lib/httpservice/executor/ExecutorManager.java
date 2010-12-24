package novoda.lib.httpservice.executor;

import java.util.Map;

import android.content.Intent;

public interface ExecutorManager {

	void shutdown();

	void addTask(Intent intent);
	
	boolean isWorking();
	
	void start();
	
	void pause();
	
	Map<String, String> dump();

}
