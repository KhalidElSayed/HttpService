
package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

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
		if(debugIsEnable()) {
    		d("Executor Service on Create");
    	}
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
	
}
