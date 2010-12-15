
package novoda.lib.httpservice.executor;

import novoda.lib.httpservice.util.LogTag;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public abstract class ExecutorService<T> extends Service implements CallableExecutor<T> {
	
	public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;
    
	private static final long SERVICE_LIFESPAN = 1000 * 60 * 3;

    private static final int MESSAGE_TIMEOUT_AFTER_FIRST_CALL = 0x2;
    
    protected ExecutorManager<T> executorManager;
    
    private Handler lifecycleHandler;
    
    public ExecutorService() {
    	this(null, null);
    }
    
    public ExecutorService(ExecutorManager<T> executorManager, Handler lifecycleHandler) {
    	if(executorManager != null) {
    		this.executorManager = executorManager; 
    	} else {
    		this.executorManager = new QueuedExecutorManager<T>(this);
    	}
    	
    	if(lifecycleHandler == null) {
    		lifecycleHandler = new LifecycleHandler(); 
    	}
    }

    @Override
    public void onCreate() {
    	if(Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.VERBOSE)) {
    		Log.v(LogTag.EXECUTOR_SERVICE, "Creating the Executor Service");
    	}
        super.onCreate();
    }

    @Override
    public void onDestroy() {
    	if(Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.VERBOSE)) {
    		Log.v(LogTag.EXECUTOR_SERVICE, "Shutting down the Executor Service");
    	}
    	executorManager.shutdown();
        super.onDestroy();
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	if(Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.VERBOSE)) {
    		Log.v(LogTag.EXECUTOR_SERVICE, "Executing intent");
    	}
        executorManager.addTask(intent);
        lifecycleHandler.sendEmptyMessage(MESSAGE_RECEIVED_REQUEST);
        return START_NOT_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    private class LifecycleHandler extends Handler { 
    	
    	private long lastCall = 0L;

    	@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MESSAGE_ADD_TO_QUEUE: {
					break;
				}
				case MESSAGE_RECEIVED_REQUEST: {
					lastCall = System.currentTimeMillis();
					sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
					break;
				}
				case MESSAGE_TIMEOUT_AFTER_FIRST_CALL: {
					if (System.currentTimeMillis() - lastCall > SERVICE_LIFESPAN && !executorManager.isWorking()) {
						if (Log.isLoggable(LogTag.EXECUTOR_SERVICE, Log.VERBOSE)) {
							Log.v(LogTag.EXECUTOR_SERVICE, "stoping service");
						}
						stopSelf();
					} else {
						sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
					}
					break;
				}
			}
		}
    }
}
