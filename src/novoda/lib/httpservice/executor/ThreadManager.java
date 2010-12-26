package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Core.w;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import novoda.lib.httpservice.exception.ExecutorException;
import novoda.lib.httpservice.executor.monitor.Monitorable;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.request.Response;
import android.content.Intent;

public class ThreadManager implements ExecutorManager {

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

	private ThreadPoolExecutor poolExecutor;

	private ExecutorCompletionService<Response> completitionService;

	private Thread looperThread;

	private CallableExecutor<Response> callableExecutor;

	private EventBus eventBus;
	
	public ThreadManager(EventBus eventBus, CallableExecutor<Response> callableExecutor) {
		this(eventBus, callableExecutor, null, null);
	}

	public ThreadManager(EventBus eventBus, CallableExecutor<Response> callableExecutor, ThreadPoolExecutor poolExecutor,
			ExecutorCompletionService<Response> completitionService) {
		if (debugIsEnable()) {
			d("Starting thread manager");
		}
		if (poolExecutor == null) {
			poolExecutor = getThreadPoolExecutor();
		}
		this.poolExecutor = poolExecutor;

		if (completitionService == null) {
			completitionService = (ExecutorCompletionService<Response>) getCompletionService(poolExecutor);
		}
		this.completitionService = completitionService;
		
		this.eventBus = eventBus;
		this.callableExecutor = callableExecutor;
	}

	@Override
	public void shutdown() {
		if (debugIsEnable()) {
			d("Shutting down looper Thread");
		}
		if(looperThread != null) {
			looperThread.interrupt();
		}
		if(poolExecutor != null) {
			poolExecutor.shutdownNow();
		}
	}

	@Override
	public void addTask(Intent intent) {
		Callable<Response> callable = callableExecutor.getCallable(intent);
		if (callable == null) {
			throw new ExecutorException(
					"The callable retrieve from the service to hanble the intent is null");
		}
		completitionService.submit(callable);
	}

	@Override
	public boolean isWorking() {
		if(poolExecutor.getActiveCount() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		if (debugIsEnable()) {
			d("Starting looperThread");
		}
		looperThread = new Thread() {
			public void run() {
				Thread.currentThread().setPriority(NORM_PRIORITY-1);
				if (debugIsEnable()) {
					d("Run looperThread");
				}
				while (!isInterrupted()) {
					try {
						if (debugIsEnable()) {
							d("Executing callable");
						}
						Response response = completitionService.take().get();
						if (debugIsEnable()) {
							d("Response received");
						}
						eventBus.fireOnContentReceived(response);
					} catch (InterruptedException e) {
						w("InterruptedException", e);
					} catch (ExecutionException e) {
						w("ExecutionException", e);
					}
				}
			};
		};
		looperThread.start();
	}

	private final ThreadPoolExecutor getThreadPoolExecutor() {
		return new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
				KEEP_ALIVE, TimeUnit.SECONDS, BLOCKING_QUEUE, THREAD_FACTORY);
	}

	private final CompletionService<Response> getCompletionService(ThreadPoolExecutor poolExecutor) {
		return new ExecutorCompletionService<Response>(poolExecutor);
	}

	@Override
	public Map<String, String> dump() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Monitorable.POOL_SIZE, Monitorable.EMPTY + poolExecutor.getPoolSize());
		map.put(Monitorable.ACTIVE_COUNT, Monitorable.EMPTY + poolExecutor.getActiveCount());
		map.put(Monitorable.TASK_COUNT, Monitorable.EMPTY + poolExecutor.getTaskCount());
		map.put(Monitorable.COMPLETED_TASKS, Monitorable.EMPTY + poolExecutor.getCompletedTaskCount());
		map.put(Monitorable.IS_SHUTDOWN, Monitorable.EMPTY + poolExecutor.isShutdown());
		map.put(Monitorable.IS_TERMINATED, Monitorable.EMPTY + poolExecutor.isTerminated());
		map.put(Monitorable.IS_TERMINATING, Monitorable.EMPTY + poolExecutor.isTerminating());
		return map;
	}

}