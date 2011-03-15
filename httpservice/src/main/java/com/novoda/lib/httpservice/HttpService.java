
package com.novoda.lib.httpservice;

import static com.novoda.lib.httpservice.utils.Log.v;
import static com.novoda.lib.httpservice.utils.Log.verboseLoggingEnabled;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.ActorNotFoundException;
import com.novoda.lib.httpservice.actor.factory.ProgrammaticActorFactory;
import com.novoda.lib.httpservice.controller.CallableWrapper;
import com.novoda.lib.httpservice.controller.LifecycleManager;
import com.novoda.lib.httpservice.controller.executor.ConnectedMultiThreadExecutor;
import com.novoda.lib.httpservice.controller.executor.Executor;
import com.novoda.lib.httpservice.provider.Provider;
import com.novoda.lib.httpservice.provider.http.HttpProvider;
import com.novoda.lib.httpservice.storage.InMemoryStorage;
import com.novoda.lib.httpservice.storage.Storage;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Callable;

/**
 * @author luigi@novoda.com
 */
public class HttpService extends Service {

    private static final String META_DATA_KEY = "com.novoda.lib.httpservice";

    private Provider provider;

    private ActorFactory actorFactory;

    private Storage storage;

    private LifecycleManager lifecycleManager;

    private Executor executor;

    public HttpService() {
        super();
        this.provider = getProvider();
        this.actorFactory = getActorFactory();
        this.storage = getStorage();
        initLifecycleManager();
        initExecutorManager();
    }

    protected ActorFactory getActorFactory() {
        return new ProgrammaticActorFactory();
    }

    protected Provider getProvider() {
        return new HttpProvider();
    }

    protected Storage getStorage() {
        return new InMemoryStorage();
    }

    private void initLifecycleManager() {
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

    private void initExecutorManager() {
        this.executor = new ConnectedMultiThreadExecutor(this);
    }

    @Override
    public void onCreate() {

        if (verboseLoggingEnabled()) {
            v("Starting HttpService");
        }

        try {
            ComponentName cn = new ComponentName(getBaseContext(), this.getClass());
            ServiceInfo serviceInfo = getBaseContext().getPackageManager().getServiceInfo(cn,
                    PackageManager.GET_META_DATA);
            Bundle bundle = serviceInfo.metaData;
            if (bundle != null && bundle.containsKey(META_DATA_KEY)) {
                String value = (String) bundle.get("org.android.androidapp.webURL");
                Log.i("TEST", value);
                // getBaseContext().getResources().getXml(id)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        lifecycleManager.startLifeCycle();
        executor.start();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (verboseLoggingEnabled()) {
            v("Executing intent");
        }
        lifecycleManager.notifyOperation();
        executor.addCallable(getCallable(intent));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if (verboseLoggingEnabled()) {
            v("Executor Service on Destroy");
        }
        lifecycleManager.stopLifeCycle();
        if (executor != null) {
            executor.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        executor.onLowMemory();
        provider.onLowMemory();
        super.onLowMemory();
    }

    private Callable<Void> getCallable(Intent intent) {
        if (verboseLoggingEnabled()) {
            v("Building up a callable with the provider and the intentWrapper");
        }
        Actor actor;
        try {
            actor = actorFactory.getActor(intent, storage);
            actor.onCreate();
            return new CallableWrapper(provider, actor);
        } catch (ActorNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean isWorking() {
        return executor.isWorking();
    }

}
