
package novoda.rest.concurrent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectedThreadPoolExecutor extends ThreadPoolExecutor {

    protected static final String TAG = "RESTProvider:concurrency";

    private boolean isPaused;

    private ReentrantLock pauseLock = new ReentrantLock();

    private Condition unpaused = pauseLock.newCondition();

    private Context context;

    private BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true)) {
                    if (Log.isLoggable(TAG, Log.INFO)) {
                        Log.i(TAG, "No connectivity pausing...");
                    }
                    pause();
                }
                if (intent.hasExtra(ConnectivityManager.EXTRA_NETWORK_INFO)) {
                    NetworkInfo info = intent
                            .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                    if (info.isConnectedOrConnecting()) {
                        if (Log.isLoggable(TAG, Log.INFO)) {
                            Log.i(TAG, "Resuming for: " + info.toString());
                        }
                        resume();
                    }
                }
            }
        }
    };

    public ConnectedThreadPoolExecutor(Context context, int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.context = context;
        init();
    }

    public ConnectedThreadPoolExecutor(Context context, int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.context = context;
        init();
    }

    public ConnectedThreadPoolExecutor(Context context, int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.context = context;
        init();
    }

    public ConnectedThreadPoolExecutor(Context context, int corePoolSize, int maximumPoolSize,
            long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.context = context;
        init();
    }

    private void init() {
        registerReceiver();
    }

    private void registerReceiver() {
        context.registerReceiver(connectivityReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
        context.registerReceiver(connectivityReceiver, new IntentFilter(
                ConnectivityManager.ACTION_BACKGROUND_DATA_SETTING_CHANGED));
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
        context.unregisterReceiver(connectivityReceiver);
    }

    protected void beforeExecute(Thread t, Runnable r) {
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
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }

}
