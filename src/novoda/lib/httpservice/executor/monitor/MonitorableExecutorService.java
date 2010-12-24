package novoda.lib.httpservice.executor.monitor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import novoda.lib.httpservice.executor.ExecutorManager;
import novoda.lib.httpservice.executor.ExecutorService;
import novoda.lib.httpservice.executor.LifecycleHandler;
import novoda.lib.httpservice.provider.EventBus;

public abstract class MonitorableExecutorService extends ExecutorService implements Monitorable {
	
	private Monitor monitor;

	private boolean runMonitor = true;
	
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
		runMonitor = true;
		new Thread() {
			public void run() {
				while (runMonitor) {
					try {					
						monitor.dump(dump());
						sleep(monitor.getInterval());
					} catch (InterruptedException e) {
						if (debugIsEnable()) {
							d("Exception during the monitor execution loop");
						}
					}
				}
			}
		}.start();
	}

	@Override
	public void stopMonitoring() {
		if (debugIsEnable()) {
			d("Stopping monitoring the executor manager");
		}
		runMonitor = false;
	}

}
