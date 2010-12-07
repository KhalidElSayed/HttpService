
package novoda.lib.httpservice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public abstract class QueuedService<T> extends Service {

    public static final String TAG = QueuedService.class.getSimpleName();

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE = 10;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;

    private static final int MESSAGE_TIMEOUT_AFTER_FIRST_CALL = 0x2;

    public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    /*
     * Service lifespan for around 3 minutes. The idea being that we should keep
     * the service up for 3 minutes before killing self.
     */
    public static final long SERVICE_LIFESPAN = 1000 * 60 * 3;

    private LifecycleHandler handler = new LifecycleHandler();

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "QueuedService #" + mCount.getAndIncrement());
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    };

    private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(10);

    private final ThreadPoolExecutor sExecutor;

    private final ExecutorCompletionService<T> completedTask;

    private Thread looperThread;

    public QueuedService() {
        sExecutor = getThreadPoolExecutor();
        completedTask = (ExecutorCompletionService<T>) getCompletionService();
        looperThread = new Thread() {
            public void run() {
                while (!isInterrupted()) {
                    try {
                        getAndRunTask();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        looperThread.start();
    }

    protected void getAndRunTask() throws InterruptedException, ExecutionException {
        Future<T> c = completedTask.take();
        onHandleResult(c.get());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        sExecutor.shutdown();
        looperThread.interrupt();
        super.onDestroy();
    }

    protected ThreadPoolExecutor getThreadPoolExecutor() {
        synchronized (this) {
            if (sExecutor == null)
                return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                        TimeUnit.SECONDS, sWorkQueue, sThreadFactory);
            else
                return sExecutor;
        }
    }

    protected CompletionService<T> getCompletionService() {
        synchronized (this) {
            if (completedTask == null)
                return new ExecutorCompletionService<T>(sExecutor);
            else
                return completedTask;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        completedTask.submit(getCallable(intent));
        handler.sendEmptyMessage(MESSAGE_RECEIVED_REQUEST);
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
            switch (msg.what) {
                case MESSAGE_ADD_TO_QUEUE:
                    break;
                case MESSAGE_RECEIVED_REQUEST:
                    lastCall = System.currentTimeMillis();
                    sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
                    break;
                case MESSAGE_TIMEOUT_AFTER_FIRST_CALL:
                    if (System.currentTimeMillis() - lastCall > SERVICE_LIFESPAN
                            && sWorkQueue.isEmpty()) {
                        if (Log.isLoggable(TAG, Log.VERBOSE)) {
                            Log.v(TAG, "stoping service");
                        }
                        stopSelf();
                    } else {
                        sendEmptyMessageDelayed(MESSAGE_TIMEOUT_AFTER_FIRST_CALL, SERVICE_LIFESPAN);
                    }
                    break;
            }
        }
    }

    protected abstract Callable<T> getCallable(final Intent intent);

    protected abstract void onHandleResult(T data);
}
