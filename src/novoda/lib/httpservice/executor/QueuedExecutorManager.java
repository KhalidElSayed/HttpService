package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;

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

import android.content.Intent;

public class QueuedExecutorManager<T> implements ExecutorManager<T>,
		Monitorable {

	private static final int CORE_POOL_SIZE = 5;

	private static final int MAXIMUM_POOL_SIZE = 5;

	private static final int KEEP_ALIVE = 5;
	
	private Monitor monitor;

	private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "QueuedService #"
					+ mCount.getAndIncrement());
			thread.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			return thread;
		}
	};

	private static LinkedBlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<Runnable>(100);

	private ThreadPoolExecutor poolExecutor;

	private ExecutorCompletionService<T> completitionService;

	private Thread looperThread;

	private CallableExecutor<T> callableExecutor;

	public QueuedExecutorManager(CallableExecutor<T> callableExecutor) {
		this(callableExecutor, null, null);
	}

	public QueuedExecutorManager(CallableExecutor<T> callableExecutor,
			ThreadPoolExecutor poolExecutor,
			ExecutorCompletionService<T> completitionService) {
		if (debugIsEnableForES()) {
			debugES("starting queued executor manager");
		}
		if (poolExecutor == null) {
			poolExecutor = getThreadPoolExecutor();
		}
		this.poolExecutor = poolExecutor;

		if (completitionService == null) {
			completitionService = (ExecutorCompletionService<T>) getCompletionService(poolExecutor);
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
		if (callable == null) {
			throw new ExecutorException(
					"The callable retrieve from the service to hanble the intent is null");
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
						throw new ExecutorException("Interrupted Exception");
					} catch (ExecutionException e) {
						throw new ExecutorException("Execution Exeption");
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

	private final CompletionService<T> getCompletionService(
			ThreadPoolExecutor poolExecutor) {
		return new ExecutorCompletionService<T>(poolExecutor);
	}

	@Override
	public void attach(Monitor monitor) {
		this.monitor = monitor;
	}

	private static final String POOL_SIZE = "PoolSize";
	
	private static final String ACTIVE_COUNT = "ActiveCount";
	
	private static final String TASK_COUNT = "TaskCount";
	
	private static final String COMPLETED_TASKS = "CompletedTask";
	
	private static final String IS_SHUTDOWN = "isShutdown";
	private static final String IS_TERMINATED = "isTerminated";
	private static final String IS_TERMINATING = "isTerminating";

	private boolean runMonitor = true;

	@Override
	public void startMonitoring() {	
		if (debugIsEnableForES()) {
			debugES("Starting monitoring the executor manager");
		}
		runMonitor = true;
		new Thread() {
			public void run() {
				while (runMonitor) {
					try {
						Map<String, String> map = new HashMap<String, String>();
						map.put(POOL_SIZE, "" + poolExecutor.getPoolSize());
						map.put(ACTIVE_COUNT, "" + poolExecutor.getActiveCount());
						map.put(TASK_COUNT, "" + poolExecutor.getTaskCount());
						map.put(COMPLETED_TASKS, "" + poolExecutor.getCompletedTaskCount());
						map.put(IS_SHUTDOWN, "" + poolExecutor.isShutdown());
						map.put(IS_TERMINATED, "" + poolExecutor.isTerminated());
						map.put(IS_TERMINATING, "" + poolExecutor.isTerminating());
						monitor.dump(map);
						sleep(monitor.getInterval());
					} catch (InterruptedException e) {
						if (debugIsEnableForES()) {
							debugES("Exception during the monitor execution loop");
						}
					}
				}
			}
		}.start();
	}

	@Override
	public void stopMonitoring() {
		if (debugIsEnableForES()) {
			debugES("Starting monitoring the executor manager");
		}
		runMonitor = false;
	}

}