
package novoda.lib.httpservice.net;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import novoda.rest.concurrent.RequestHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

public class AsyncHttpClient {

    private final HttpClient client;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "QueuedService #" + mCount.getAndIncrement());
            thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return thread;
        }
    };

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE = 10;

    private static final int MESSAGE_RECEIVED_REQUEST = 0x1;

    private static final int MESSAGE_TIMEOUT_AFTER_FIRST_CALL = 0x2;

    public static final int MESSAGE_ADD_TO_QUEUE = 0x3;

    private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(10);

    private final ThreadPoolExecutor sExecutor;

    private final ExecutorCompletionService completedTask;

    public AsyncHttpClient(HttpClient client) {
        this.client = client;
        sExecutor = getThreadPoolExecutor();
        completedTask = (ExecutorCompletionService) getCompletionService();
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

    protected CompletionService getCompletionService() {
        synchronized (this) {
            if (completedTask == null)
                return new ExecutorCompletionService(sExecutor);
            else
                return completedTask;
        }
    }

    public <T> Future<T> execute(HttpUriRequest request, RequestHandler<T> handler) {
       return completedTask.submit(new AsyncRequest<T>(request, handler));
        //return new TestFuture<T>(new AsyncRequest<T>(request, handler));
    }

    class TestFuture<T> extends FutureTask<T> {
        public TestFuture(Callable<T> callable) {
            super(callable);
        }
    }

    private class AsyncRequest<T> implements Callable<T> {

        private final HttpUriRequest request;

        private final RequestHandler<T> handler;

        public AsyncRequest(HttpUriRequest request, RequestHandler<T> handler) {
            this.request = request;
            this.handler = handler;
        }

        @Override
        public T call() throws Exception {
            return client.execute(request, handler);
        }
    }
}
