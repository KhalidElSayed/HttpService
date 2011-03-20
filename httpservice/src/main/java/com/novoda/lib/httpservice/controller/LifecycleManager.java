package com.novoda.lib.httpservice.controller;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;
import static com.novoda.lib.httpservice.utils.Log.w;

import java.util.Timer;
import java.util.TimerTask;

<<<<<<< HEAD:httpservice/src/main/java/com/novoda/lib/httpservice/controller/LifecycleManager.java
public abstract class LifecycleManager {
=======
import novoda.lib.httpservice.Settings;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.provider.IntentRegistry;
import novoda.lib.httpservice.service.executor.ExecutorManager;
import android.content.Intent;

public abstract class LifecycleManagedExecutorService extends ExecutorService {
>>>>>>> 4dc03d6d784dfcf9a7ae2f39b2754102fab5eb0a:src/novoda/lib/httpservice/service/LifecycleManagedExecutorService.java
	
	private static final long SERVICE_LIFECYCLE = 1000 * 30;
	
	private static final long KEEP_ALIFE_TIME = 1000 * 60 * 5;
	
	private long lastCall;
	
	private Timer timer;
	
<<<<<<< HEAD:httpservice/src/main/java/com/novoda/lib/httpservice/controller/LifecycleManager.java
	public void notifyOperation() {
		lastCall = System.currentTimeMillis();
=======
	public LifecycleManagedExecutorService(IntentRegistry requestRegistry, EventBus eventBus, ExecutorManager executorManager, Settings settings) {
		super(requestRegistry, eventBus, executorManager, settings);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		lastCall = System.currentTimeMillis();		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		if (verboseLoggingEnabled()) {
			v("Lifecycle manager: Starting lifecycle");
		}
		startLifeCycle();
		super.onCreate();
>>>>>>> 4dc03d6d784dfcf9a7ae2f39b2754102fab5eb0a:src/novoda/lib/httpservice/service/LifecycleManagedExecutorService.java
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
					if (verboseLoggingEnabled()) {
						v("Lifecycle manager: working? " + working + " last execution was? " + delta);
					}
					if (working || delta < KEEP_ALIFE_TIME) {
						if (verboseLoggingEnabled()) {
							v("Lifecycle manager: keeping alive the service");
						}      
					} else {
						if (verboseLoggingEnabled()) {
							v("Lifecycle manager: stoping service");
						}		
						stopLifeCycle();
						stop();
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
			if (verboseLoggingEnabled()) {
				v("Lifecycle manager: removing timer");
			}
			timer.cancel();
			timer.purge();
		} catch(Throwable t) {
			w("Lifecycle manager: Cancel on a not scheduled timer", t);
		}
	}

	protected abstract void stop();
	
	protected abstract boolean isWorking();
	
}
