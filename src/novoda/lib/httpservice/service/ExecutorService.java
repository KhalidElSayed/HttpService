
package novoda.lib.httpservice.service;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.Map;

import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.HasHandlers;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.executor.CallableExecutor;
import novoda.lib.httpservice.service.executor.ConnectedThreadPoolExecutor;
import novoda.lib.httpservice.service.executor.ExecutorManager;
import novoda.lib.httpservice.service.executor.ThreadManager;
import novoda.lib.httpservice.service.monitor.Dumpable;
import novoda.lib.httpservice.service.monitor.Monitor;
import novoda.lib.httpservice.service.monitor.MonitorManager;
import novoda.lib.httpservice.service.monitor.Monitorable;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Main Executor Service.
 * 
 * @author luigi
 *
 * @param <T>
 */
public abstract class ExecutorService extends Service implements CallableExecutor<Response>, HasHandlers, Dumpable, Monitorable {
    
    protected ExecutorManager executorManager;
    
    private MonitorManager monitorManager;
    
    protected EventBus eventBus;
    
    public ExecutorService(EventBus eventBus, ExecutorManager executorManager) {
    	super();
    	this.eventBus = eventBus;
    	if(this.eventBus == null) {
    		this.eventBus = new EventBus(); 
    	}
    	this.executorManager = executorManager; 
    	if(this.executorManager == null) {
    		ConnectedThreadPoolExecutor pool = new ConnectedThreadPoolExecutor(this);
    		this.executorManager = new ThreadManager(pool, this.eventBus, this);
    	}
    	this.monitorManager = new MonitorManager(this);
    }

	@Override
    public void onCreate() {
		if(debugIsEnable()) {
    		d("Executor Service on Create");
    	}
		executorManager.start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
    	if(debugIsEnable()) {
    		d("Executor Service on Destroy");
    	}
    	stopMonitoring();
    	if(executorManager != null) {
    		executorManager.shutdown();
    	}
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(debugIsEnable()) {
    		d("Executing intent");
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
	//Relative handlers for the eventBus
	//==============================================================
	
	@Override
	public void addGlobalHandler(String key, GlobalHandler handler) {
		eventBus.addGlobalHandler(key, handler);
	}
	
	@Override
	public void removeGlobalHandler(String key, GlobalHandler handler) {
		eventBus.removeGlobalHandler(key, handler);
	}

	@Override
	public void addRequestHandler(String key, RequestHandler handler) {
		eventBus.addRequestHandler(key, handler);
	}

	@Override
	public void removeRequestHandler(String key, RequestHandler handler) {
		eventBus.removeRequestHandler(key, handler);
	}

	@Override
	public void addGlobalHandler(GlobalHandler handler) {
		eventBus.addGlobalHandler(handler);
	}
	
	@Override
	public void removeGlobalHandler(GlobalHandler handler) {
		eventBus.removeGlobalHandler(handler);
	}

	@Override
	public void addRequestHandler(RequestHandler handler) {
		eventBus.addRequestHandler(handler);
	}

	@Override
	public void removeRequestHandler(RequestHandler handler) {
		eventBus.removeRequestHandler(handler);
	}
}
