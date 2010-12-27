package novoda.lib.httpservice.service;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Core.w;

import java.util.Timer;
import java.util.TimerTask;

import novoda.lib.httpservice.provider.EventBus;
import novoda.lib.httpservice.service.executor.ExecutorManager;
import novoda.lib.httpservice.service.monitor.Monitor;
import novoda.lib.httpservice.service.monitor.Monitorable;

public abstract class MonitorableExecutorService extends LifecycleManagedExecutorService implements Monitorable {
	
	private Monitor monitor;
	
	private Timer timer;
	
	public MonitorableExecutorService() {
		this(null, null);
	}
	
	public MonitorableExecutorService(EventBus eventBus, ExecutorManager executorManager) {
		super(eventBus, executorManager);
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
			if(timer != null) {
				timer.cancel();
				timer.purge();
			}
		} catch(Throwable t) {
			w("Cancel on a not scheduled timer", t);
		}
	}

}
