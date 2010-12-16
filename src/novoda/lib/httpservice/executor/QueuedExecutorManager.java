package novoda.lib.httpservice.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


import android.content.Intent;

public class QueuedExecutorManager<T> implements ExecutorManager<T> {
	
    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 128;

    private static final int KEEP_ALIVE = 10;

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);
		
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "QueuedService #" + mCount.getAndIncrement());
			thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			return thread;
		}
	};
	
    private static LinkedBlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<Runnable>(10);

    private ThreadPoolExecutor poolExecutor;

    private ExecutorCompletionService<T> completitionService;

    private Thread looperThread;
    
    private CallableExecutor<T> callableExecutor;
	
	public QueuedExecutorManager(CallableExecutor<T> callableExecutor) {
		this(callableExecutor, null, null);
	}

	public QueuedExecutorManager(CallableExecutor<T> callableExecutor, ThreadPoolExecutor poolExecutor, ExecutorCompletionService<T> completitionService) {
		if(poolExecutor == null) {    		
    		poolExecutor = getThreadPoolExecutor();
    	}
    	this.poolExecutor = poolExecutor;
    	
    	if(completitionService == null) {    		
    		completitionService = (ExecutorCompletionService<T>)getCompletionService(poolExecutor);
    	}
    	this.completitionService = completitionService;
    	
    	this.callableExecutor = callableExecutor;
	}

	@Override
	public void shutdown() {
		looperThread.interrupt();
		poolExecutor.shutdown();
	}
	
	@Override
	public void addTask(Intent intent) {
		Callable<T> callable = callableExecutor.getCallable(intent);
		if(callable == null) {
			throw new ExecutorException("The callable retrieve from the service to hanble the intent is null");
		}
		completitionService.submit(callable);
	}
	
	@Override
	public boolean isWorking() {
		return !(poolExecutor.isTerminated() || poolExecutor.isTerminating());
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		looperThread = new Thread() {
	        public void run() {
	            while (!isInterrupted()) {
	                try {
	                	completitionService.take().get();
	                } catch (InterruptedException e) {
	                	e.printStackTrace();
	                	throw new ExecutorException("Interrupted Exception");
	                } catch (ExecutionException e) {
	                	e.printStackTrace();
	                	throw new ExecutorException("Execution Exeption");
	                }
	            }
	        };
	    };
	    looperThread.start();
	}
	
    private final ThreadPoolExecutor getThreadPoolExecutor() {
    	return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                        TimeUnit.SECONDS, BLOCKING_QUEUE, THREAD_FACTORY);
    }

    private final CompletionService<T> getCompletionService(ThreadPoolExecutor poolExecutor) {
        return new ExecutorCompletionService<T>(poolExecutor);
    }

}