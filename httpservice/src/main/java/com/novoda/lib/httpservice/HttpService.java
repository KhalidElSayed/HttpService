package com.novoda.lib.httpservice;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;

import java.util.concurrent.Callable;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.novoda.lib.httpservice.config.Config;
import com.novoda.lib.httpservice.config.ManualConfig;
import com.novoda.lib.httpservice.controller.CallableWrapper;
import com.novoda.lib.httpservice.controller.LifecycleManager;
import com.novoda.lib.httpservice.controller.executor.ConnectedMultiThreadExecutor;
import com.novoda.lib.httpservice.controller.executor.Executor;
import com.novoda.lib.httpservice.provider.Provider;
import com.novoda.lib.httpservice.provider.http.HttpProvider;
import com.novoda.lib.httpservice.storage.InMemoryStorage;
import com.novoda.lib.httpservice.storage.Storage;

/**
 * @author luigi@novoda.com
 */
public class HttpService extends Service {
	
	private Provider provider;
	private Config config;
	private Storage storage;
	private LifecycleManager lifecycleManager;
	private Executor executor;

	public HttpService() {
		super();
		initProvider();
		initConfig();
		initStorage();
		initLifecycleManager();
		initExecutorManager();
	}
	
	protected void initProvider() {
		this.provider = new HttpProvider();
	}
	
	protected void initConfig() {
		this.config = new ManualConfig();
	}
	
	protected void initStorage() {
		this.storage = new InMemoryStorage();
	}
	
	protected void initLifecycleManager() {
		this.lifecycleManager = new LifecycleManager() {
			@Override
			protected boolean isWorking() {
				return HttpService.this.isWorking();
			}
			@Override
			protected void stop() {
				stopSelf();
			}			
		};
	}

	protected void initExecutorManager() {
		this.executor = new ConnectedMultiThreadExecutor(this);
    }
	
	@Override
	public void onCreate() {
		if (verboseLoggingEnabled()) {
			v("Starting HttpService");
		}
		lifecycleManager.startLifeCycle();
		executor.start();
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(verboseLoggingEnabled()) {
			v("Executing intent");
		}
		lifecycleManager.notifyOperation();	
        executor.addCallable(getCallable(intent));
        return START_NOT_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if(verboseLoggingEnabled()) {
			v("Executor Service on Destroy");
		}
		lifecycleManager.stopLifeCycle();
    	if(executor != null) {
    		executor.shutdown();
    	}
		super.onDestroy();
	}
	
	private Callable<Void> getCallable(Intent intent) {
		if (verboseLoggingEnabled()) {
			v("Building up a callable with the provider and the intentWrapper");
		}
		return new CallableWrapper(provider, config.getActor(intent, storage));
	}
	
	@Override
    public IBinder onBind(Intent intent) {
        return null;
    }
	
	private boolean isWorking() {
		return executor.isWorking();
	}
	
}
