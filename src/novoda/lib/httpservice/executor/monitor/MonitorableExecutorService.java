package novoda.lib.httpservice.executor.monitor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.w;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.Timer;
import java.util.TimerTask;

import novoda.lib.httpservice.executor.ExecutorManager;
import novoda.lib.httpservice.executor.ExecutorService;
import novoda.lib.httpservice.executor.LifecycleHandler;
import novoda.lib.httpservice.provider.EventBus;

public abstract class MonitorableExecutorService extends ExecutorService implements Monitorable {
	
	private Monitor monitor;
	
	private Timer timer;
	
	public MonitorableExecutorService() {
		this(null, null, null);
	}
	
	public MonitorableExecutorService(EventBus eventBus, ExecutorManager executorManager, LifecycleHandler lifecycleHandler) {
		super(eventBus, executorManager, lifecycleHandler);
	}
	
	@Override
	public void attach(Monitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void startMonitoring() {	
		if (debugIsEnable()) {
			d("Starting monitoring the executor manager");
		}
		try {
			timer = new Timer();
			TimerTask monitorThread = new TimerTask() {
				@Override
				public void run() {
					monitor.dump(dump());
				}
			};
			timer.schedule(monitorThread, 0, monitor.getInterval());
		} catch(Throwable t) {
			w("Scheduling timer already scheduled", t);
		}
	}

	@Override
	public void stopMonitoring() {
		try {
			timer.cancel();
			timer.purge();
		} catch(Throwable t) {
			w("Cancel on a not scheduled timer", t);
		}
	}

}
