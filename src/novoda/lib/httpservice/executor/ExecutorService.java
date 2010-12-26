
package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.Map;

import novoda.lib.httpservice.handler.GlobalHandler;
import novoda.lib.httpservice.handler.HasHandlers;
import novoda.lib.httpservice.handler.RequestHandler;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.request.Response;
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
public abstract class ExecutorService extends Service implements CallableExecutor<Response>, HasHandlers {
    
    protected ExecutorManager executorManager;
    
    protected EventBus eventBus;
    
    private LifecycleHandler lifecycleHandler;
    
    public ExecutorService() {
    	this(null, null, null);
    }
    
    public ExecutorService(EventBus eventBus, ExecutorManager executorManager, LifecycleHandler lifecycleHandler) {
    	this.eventBus = eventBus;
    	if(this.eventBus == null) {
    		this.eventBus = new EventBus(); 
    	}

    	this.executorManager = executorManager; 
    	if(this.executorManager == null) {
    		this.executorManager = new ThreadManager(this.eventBus, this);
    	}
    	
    	this.lifecycleHandler = lifecycleHandler; 
    	if(this.lifecycleHandler == null) {
    		this.lifecycleHandler = new LifecycleHandler(this); 
    	}
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
        lifecycleHandler.messageReceived();
        return START_NOT_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * The use of this method can be expensive!
     * @return
     */
    public Map<String, String> dump() {
    	return executorManager.dump();
    }

	public boolean isWorking() {
		return executorManager.isWorking();
	}
	
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
