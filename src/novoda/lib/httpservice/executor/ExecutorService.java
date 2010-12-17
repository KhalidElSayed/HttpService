
package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public abstract class ExecutorService<T> extends Service implements CallableExecutor<T>, Monitorable {
	
	public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;
    
	private static final long SERVICE_LIFESPAN = 1000 * 30;

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
    		this.lifecycleHandler = new LifecycleHandler(); 
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
    		if(debugIsEnableForES()) {
        		debugES("LifecycleHandler : " + msg);
        	}
			switch (msg.what) {
				case MESSAGE_ADD_TO_QUEUE: {
					if (debugIsEnableForES()) {
						debugES("Add to queue");
					}
					break;
				}
				case MESSAGE_RECEIVED_REQUEST: {
					if (debugIsEnableForES()) {
						debugES("Message received request");
					}
					lastCall = System.currentTimeMillis();
					sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
					break;
				}
				case MESSAGE_TIMEOUT_AFTER_FIRST_CALL: {
					if (debugIsEnableForES()) {
						debugES("Message timeout after first call");
					}
					if (System.currentTimeMillis() - lastCall > SERVICE_LIFESPAN && !executorManager.isWorking()) {
						if (debugIsEnableForES()) {
							debugES("stoping service");
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
    
    @Override
    public void attach(Monitor monitor) {
    	if(executorManager instanceof Monitorable) {    		
    		((Monitorable)executorManager).attach(monitor);
    	}
    }
    
	@Override
	public void startMonitoring() {
		if(executorManager instanceof Monitorable) {    		
    		((Monitorable)executorManager).startMonitoring();
    	}
	}

	@Override
	public void stopMonitoring() {
		if(executorManager instanceof Monitorable) {    		
    		((Monitorable)executorManager).stopMonitoring();
    	}
	}
}
