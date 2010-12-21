package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;
import android.os.Handler;
import android.os.Message;

public class LifecycleHandler extends Handler { 
	
	public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;
    
	private static final long SERVICE_LIFESPAN = 1000 * 30;
	
	private static final long KEEP_ALIFE_TIME = 1000 * 60;

    private static final int MESSAGE_TIMEOUT_AFTER_CALL = 0x2;
    
    private long lastCall;
	
	private ExecutorService<?> executorService;
	
	public LifecycleHandler(ExecutorService<?> executorService) {
		this.executorService = executorService;
	}
	
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
				sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_CALL, SERVICE_LIFESPAN);
				break;
			}
			case MESSAGE_TIMEOUT_AFTER_CALL: {
				boolean working = executorService.isWorking();
				long delta = System.currentTimeMillis() - lastCall;
				if (debugIsEnableForES()) {
					debugES("Message service life timeout after call checking if is working : " + working + " and delta is " + delta);
				}
				if (working || delta < KEEP_ALIFE_TIME) {
					if (debugIsEnableForES()) {
						debugES("Keeping alive the service");
					}
					sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_CALL, SERVICE_LIFESPAN);					
				} else {
					if (debugIsEnableForES()) {
						debugES("Stoping service");
					}
					executorService.stopSelf();
				}
				break;
			}
		}
	}

	public void messageReceived() {
		sendEmptyMessage(MESSAGE_RECEIVED_REQUEST);
	}

}
