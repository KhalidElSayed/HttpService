package novoda.lib.httpservice.service.executor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This is a copy of the same class from the RestProvider project,
 * extends the ThreadPoolExecutor with the ability to react to 
 * connectivity changes
 * 
 * @author luigi@novoda.com
 *
 */
public class ConnectedThreadPoolExecutor extends ThreadPoolExecutor {
	
	private static final int CORE_POOL_SIZE = 5;

	private static final int MAXIMUM_POOL_SIZE = 5;

	private static final int KEEP_ALIVE = 5;
	
	private static final String PREFIX = "HttpService #";
	
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, PREFIX + mCount.getAndIncrement());
			thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			return thread;
		}
	};
	
	private static LinkedBlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<Runnable>(100);

    private boolean isPaused;
    
    private boolean receiverNotReady = true;
    
    private Service service;

    private ReentrantLock pauseLock = new ReentrantLock();

    private Condition unpaused = pauseLock.newCondition();

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true)) {
                    if (debugIsEnable()) {
                        d("ThreadPool : No connectivity pausing...");
                    }
                    pause();
                }
                if (intent.hasExtra(ConnectivityManager.EXTRA_NETWORK_INFO)) {
                    NetworkInfo info = intent
                            .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (info.isConnectedOrConnecting()) {
                        if (debugIsEnable()) {
                            d("ThreadPool : Connectivity is back, resuming for: " + info.toString());
                        }
                        resume();
                    }
                }
            }
        }
    };

    public ConnectedThreadPoolExecutor(Service service) {
        super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, BLOCKING_QUEUE, THREAD_FACTORY);
        this.service = service;
    }

    public void start() {
        registerReceiver();
    }

    private void registerReceiver() {
    	if (debugIsEnable()) {
			d("ThreadPool : Registering receivers");
		}
    	service.registerReceiver(connectivityReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    	service.registerReceiver(connectivityReceiver, new IntentFilter(
                ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED));
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
    	if (debugIsEnable()) {
			d("ThreadPool : unregistering receivers");
		}
    	service.unregisterReceiver(connectivityReceiver);
    	receiverNotReady = true;
    }

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
    	if (debugIsEnable()) {
			d("ThreadPool : Pausing");
		}
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
    	if (debugIsEnable()) {
			d("ThreadPool : Resuming");
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