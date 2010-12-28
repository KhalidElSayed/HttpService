package novoda.lib.httpservice.service.executor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Core.w;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import novoda.lib.httpservice.exception.ExecutorException;
import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.request.Response;
import novoda.lib.httpservice.service.monitor.Monitorable;
import android.content.Intent;

public class ThreadManager implements ExecutorManager {

	private ThreadPoolExecutor poolExecutor;

	private ExecutorCompletionService<Response> completitionService;

	private Thread looperThread;

	private CallableExecutor<Response> callableExecutor;

	private EventBus eventBus;
	
	private boolean runLoop = true;
	
	public ThreadManager(ThreadPoolExecutor poolExecutor, EventBus eventBus, CallableExecutor<Response> callableExecutor) {
		this(poolExecutor, eventBus, callableExecutor, null);
	}

	public ThreadManager(ThreadPoolExecutor poolExecutor, EventBus eventBus, CallableExecutor<Response> callableExecutor,
			ExecutorCompletionService<Response> completitionService) {
		if (debugIsEnable()) {
			d("Starting thread manager");
		}
		if (completitionService == null) {
			completitionService = (ExecutorCompletionService<Response>) getCompletionService(poolExecutor);
		}
		this.completitionService = completitionService;
		
		this.poolExecutor = poolExecutor;		
		this.eventBus = eventBus;
		this.callableExecutor = callableExecutor;
	}

	@Override
	public void shutdown() {
		if(poolExecutor != null) {
			if (debugIsEnable()) {
				d("Shutting down pool executor");
			}
			poolExecutor.shutdown();
			while(poolExecutor.isTerminating()) {
				if (debugIsEnable()) {
					d("Thread Manager : waiting for shut down of poolExecutor...");
				}
			}
			if (debugIsEnable()) {
				d("Thread Manager : poolExecutor is terminated...");
			}
		}
		if(looperThread != null) {
			if (debugIsEnable()) {
				d("Thread Manager : Shutting down looperThread");
			}
			runLoop = false;
			if (debugIsEnable()) {
				d("Thread Manager : looperThread is terminated");
			}
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
			d("Thread Manager : Starting Thread Loop");
		}
		looperThread = new Thread() {
			public void run() {
				Thread.currentThread().setPriority(NORM_PRIORITY-1);
				if (debugIsEnable()) {
					d("Thread Manager : is running now");
				}
				while (runLoop) {
					try {
						if (debugIsEnable()) {
							d("Thread Manager : new cycle");
						}
						Future<Response> future = completitionService.take();
						Response response = future.get();
						future.cancel(true);
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
				if (debugIsEnable()) {
					d("Thread Manager : ending cycle");
				}
			};
		};
		looperThread.start();
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