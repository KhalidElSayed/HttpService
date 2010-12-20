
package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;

import java.util.Map;

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
public abstract class ExecutorService<T> extends Service implements CallableExecutor<T> {
    
    protected ExecutorManager<T> executorManager;
    
    private LifecycleHandler lifecycleHandler;
    
    public ExecutorService() {
    	this(null, null);
    }
    
    public ExecutorService(ExecutorManager<T> executorManager, LifecycleHandler lifecycleHandler) {
    	this.executorManager = executorManager; 
    	if(this.executorManager == null) {
    		this.executorManager = new ThreadManager<T>(this);
    	}
    	
    	this.lifecycleHandler = lifecycleHandler; 
    	if(this.lifecycleHandler == null) {
    		this.lifecycleHandler = new LifecycleHandler(this); 
    	}
    }

	@Override
    public void onCreate() {
		if(debugIsEnableForES()) {
    		debugES("Executor Service on Create");
    	}
        super.onCreate();
    }

    @Override
    public void onDestroy() {
    	if(debugIsEnableForES()) {
    		debugES("Executor Service on Destroy");
    	}
    	executorManager.shutdown();
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(debugIsEnableForES()) {
    		debugES("Executing intent");
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
	
}
