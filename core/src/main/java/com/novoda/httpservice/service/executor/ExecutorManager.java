package com.novoda.httpservice.service.executor;

import com.novoda.httpservice.service.monitor.Dumpable;
import android.content.Intent;

public interface ExecutorManager extends Dumpable{

	void shutdown();

	void addTask(Intent intent);
	
	boolean isWorking();
	
	void start();
	
	void pause();

}
