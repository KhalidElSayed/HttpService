package novoda.lib.httpservice.executor;

import android.content.Intent;

public interface ExecutorManager<T> {

	void shutdown();

	void addTask(Intent intent);
	
	boolean isWorking();
	
	void start();
	
	void pause();

}
