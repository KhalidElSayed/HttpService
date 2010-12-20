package novoda.lib.httpservice.executor;

import static novoda.lib.httpservice.util.LogTag.debugES;
import static novoda.lib.httpservice.util.LogTag.debugIsEnableForES;

public abstract class MonitorableExecutorService<T> extends ExecutorService<T> implements Monitorable {
	
	private Monitor monitor;

	private boolean runMonitor = true;

	@Override
	public void attach(Monitor monitor) {
		this.monitor = monitor;
	}

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
						monitor.dump(dump());
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
