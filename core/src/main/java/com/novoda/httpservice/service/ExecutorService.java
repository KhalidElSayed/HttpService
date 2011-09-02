package com.novoda.httpservice.service;

import static com.novoda.httpservice.util.Log.v;
import static com.novoda.httpservice.util.Log.verboseLoggingEnabled;

import java.util.Map;

import com.novoda.httpservice.Settings;
import com.novoda.httpservice.handler.HasHandlers;
import com.novoda.httpservice.handler.RequestHandler;
import com.novoda.httpservice.processor.HasProcessors;
import com.novoda.httpservice.processor.Processor;
import com.novoda.httpservice.provider.EventBus;
import com.novoda.httpservice.provider.IntentRegistry;
import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.service.executor.CallableExecutor;
import com.novoda.httpservice.service.executor.ConnectedThreadPoolExecutor;
import com.novoda.httpservice.service.executor.ExecutorManager;
import com.novoda.httpservice.service.executor.ThreadManager;
import com.novoda.httpservice.service.monitor.Dumpable;
import com.novoda.httpservice.service.monitor.Monitor;
import com.novoda.httpservice.service.monitor.MonitorManager;
import com.novoda.httpservice.service.monitor.Monitorable;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class ExecutorService extends Service implements CallableExecutor<Response>, HasHandlers, HasProcessors, Dumpable, Monitorable {
    
    protected ExecutorManager executorManager;
    
    private MonitorManager monitorManager;
    
    protected EventBus eventBus;
    
    protected IntentRegistry requestRegistry;
    
    public ExecutorService(IntentRegistry requestRegistry, EventBus eventBus, ExecutorManager executorManager, Settings settings) {
    	super();
    	this.requestRegistry = requestRegistry;
    	if(this.requestRegistry == null) {
    		this.requestRegistry = new IntentRegistry(); 
    	}
    	this.eventBus = eventBus;
    	if(this.eventBus == null) {
    		this.eventBus = new EventBus(this.requestRegistry); 
    	}
    	this.executorManager = executorManager; 
    	if(this.executorManager == null) {
    		ConnectedThreadPoolExecutor pool = new ConnectedThreadPoolExecutor(this, settings);
    		this.executorManager = new ThreadManager(this.requestRegistry, pool, this.eventBus, this);
    	}
    	this.monitorManager = new MonitorManager(this);
    }

	@Override
    public void onCreate() {
		if(verboseLoggingEnabled()) {
    		v("Executor Service on Create");
    	}
		executorManager.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
    	if(verboseLoggingEnabled()) {
    		v("Executor Service on Destroy");
    	}
    	stopMonitoring();
    	if(executorManager != null) {
    		executorManager.shutdown();
    	}
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(verboseLoggingEnabled()) {
    		v("Executing intent");
    	}
        executorManager.addTask(intent);
        return START_NOT_STICKY;
    }

	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }
	
	public boolean isWorking() {
		return executorManager.isWorking();
	}
	
	//==============================================================
	//Relative to the monitor functionalities
	//==============================================================
    
	@Override
    public Map<String, String> dump() {
    	return executorManager.dump();
    }
	
	@Override
	public void startMonitoring() {
		monitorManager.startMonitoring();
	}
	
	@Override
	public void stopMonitoring() {
		monitorManager.stopMonitoring();
	}
	
	@Override
	public void attach(Monitor monitor) {
		monitorManager.attach(monitor);
	}

	//==============================================================
	//Relative handlers and processors for the eventBus
	//==============================================================
	
	@Override
	public void add(RequestHandler handler) {
		eventBus.add(handler);
	}

	@Override
	public void remove(RequestHandler handler) {
		eventBus.remove(handler);
	}
	
	@Override
	public void add(Processor processor) {
		eventBus.add(processor);
	}

	@Override
	public void remove(Processor processor) {
		eventBus.remove(processor);
	}
	
}
