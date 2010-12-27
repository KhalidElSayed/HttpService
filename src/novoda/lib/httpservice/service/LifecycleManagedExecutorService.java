package novoda.lib.httpservice.service;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Core.w;

import java.util.Timer;
import java.util.TimerTask;

import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.service.executor.ExecutorManager;

import android.content.Intent;

public abstract class LifecycleManagedExecutorService extends ExecutorService {
	
	private static final long SERVICE_LIFECYCLE = 1000 * 30;
	
	private static final long KEEP_ALIFE_TIME = 1000 * 25;
	
	private long lastCall;
	
	private Timer timer;
	
	public LifecycleManagedExecutorService() {
		this(null, null);
	}
	
	public LifecycleManagedExecutorService(EventBus eventBus, ExecutorManager executorManager) {
		super(eventBus, executorManager);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lastCall = System.currentTimeMillis();		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		if (debugIsEnable()) {
			d("Lifecycle manager: Starting lifecycle");
		}
		startLifeCycle();
		super.onCreate();
	}
	
	public void startLifeCycle() {	
		try {
			lastCall = System.currentTimeMillis();
			timer = new Timer();
			TimerTask monitorThread = new TimerTask() {
				@Override
				public void run() {
					boolean working = isWorking();
					long delta = System.currentTimeMillis() - lastCall;
					if (debugIsEnable()) {
						d("Lifecycle manager: working? " + working + " last execution was? " + delta);
					}
					if (working || delta < KEEP_ALIFE_TIME) {
						if (debugIsEnable()) {
							d("Lifecycle manager: keeping alive the service");
						}			
					} else {
						if (debugIsEnable()) {
							d("Lifecycle manager: stoping service");
						}
						stopLifeCycle();
						stopSelf();
					}
				}
			};
			timer.schedule(monitorThread, 0, SERVICE_LIFECYCLE);
		} catch(Throwable t) {
			w("Lifecycle manager: Scheduling timer already scheduled", t);
		}
	}

	public void stopLifeCycle() {
		try {
			if (debugIsEnable()) {
				d("Lifecycle manager: removing timer");
			}
			timer.cancel();
			timer.purge();
		} catch(Throwable t) {
			w("Lifecycle manager: Cancel on a not scheduled timer", t);
		}
	}
	
}
