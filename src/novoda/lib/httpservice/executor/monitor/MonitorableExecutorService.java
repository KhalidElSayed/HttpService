package novoda.lib.httpservice.executor.monitor;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import novoda.lib.httpservice.executor.ExecutorService;

public abstract class MonitorableExecutorService<T> extends ExecutorService<T> implements Monitorable {
	
	private Monitor monitor;

	private boolean runMonitor = true;
	
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
			d("Starting monitoring the executor manager");
		}
		runMonitor = false;
	}

}
