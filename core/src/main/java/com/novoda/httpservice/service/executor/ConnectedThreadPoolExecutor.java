package com.novoda.httpservice.service.executor;

import static com.novoda.httpservice.util.Log.v;
import static com.novoda.httpservice.util.Log.verboseLoggingEnabled;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.novoda.httpservice.Settings;
import com.novoda.httpservice.util.ConnectivityReceiver;
import android.app.Service;

public class ConnectedThreadPoolExecutor extends ThreadPoolExecutor {
	
	private static final String PREFIX = "HttpService #";
	
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, PREFIX + mCount.getAndIncrement());
			thread.setPriority(Thread.NORM_PRIORITY-1);
			return thread;
		}
	};
	
	private static LinkedBlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<Runnable>(100);

    private boolean isPaused;
    
    private boolean receiverNotReady = true;
    
    private Service service;

    private ReentrantLock pauseLock = new ReentrantLock();

    private Condition unpaused = pauseLock.newCondition();

    private ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver() {
		@Override
		protected void onConnectionLost() {
			if (verboseLoggingEnabled()) {
                v("ThreadPool : Connection lost");
            }
			pause();
		}
		
		@Override
		protected void onConnectionResume() {
			if (verboseLoggingEnabled()) {
                v("ThreadPool : Connection resumed");
            }
			resume();
		}
    };

    public ConnectedThreadPoolExecutor(Service service, Settings settings) {
        super(settings.corePoolSize, settings.maximunPoolSize, 
        		settings.keepAlive, TimeUnit.SECONDS, BLOCKING_QUEUE, THREAD_FACTORY);
        this.service = service;
    }

    public void start() {
        registerReceiver();
    }

    private void registerReceiver() {
    	if (verboseLoggingEnabled()) {
			v("ThreadPool : Registering receivers");
		}
    	service.registerReceiver(connectivityReceiver, ConnectivityReceiver.CONNECTIVITY_FILTER);
    	service.registerReceiver(connectivityReceiver, ConnectivityReceiver.SETTING_CHANGED_FILTER);
    	receiverNotReady = false;
    }
    
    
	@Override
    public void shutdown() {
        removeReceiver();
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        removeReceiver();
        return super.shutdownNow();
    }

    private void removeReceiver() {
    	if (verboseLoggingEnabled()) {
			v("ThreadPool : unregistering receivers");
		}
    	service.unregisterReceiver(connectivityReceiver);
    	receiverNotReady = true;
    }

    @Override
	protected void beforeExecute(Thread t, Runnable r) {
		if(receiverNotReady) {
			start();
		}
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused)
                unpaused.await();
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
    	if (verboseLoggingEnabled()) {
			v("ThreadPool : Pausing");
		}
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
    	if (verboseLoggingEnabled()) {
			v("ThreadPool : Resuming");
		}
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

}