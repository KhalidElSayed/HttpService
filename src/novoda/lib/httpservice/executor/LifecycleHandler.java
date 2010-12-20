package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;
import android.os.Handler;
import android.os.Message;

public class LifecycleHandler extends Handler { 
	
	public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;
    
	private static final long SERVICE_LIFESPAN = 1000 * 30;

    private static final int MESSAGE_TIMEOUT_AFTER_FIRST_CALL = 0x2;
	
	private long lastCall = 0L;
	
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
				sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
				break;
			}
			case MESSAGE_TIMEOUT_AFTER_FIRST_CALL: {
				if (debugIsEnableForES()) {
					debugES("Message timeout after first call");
				}
				if (System.currentTimeMillis() - lastCall > SERVICE_LIFESPAN && !executorService.isWorking()) {
					if (debugIsEnableForES()) {
						debugES("stoping service");
					}
					executorService.stopSelf();
				} else {
					sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
				}
				break;
			}
		}
	}

	public void messageReceived() {
		sendEmptyMessage(MESSAGE_RECEIVED_REQUEST);
	}

}
