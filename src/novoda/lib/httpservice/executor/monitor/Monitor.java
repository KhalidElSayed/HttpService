package novoda.lib.httpservice.executor.monitor;

import java.util.Map;

public interface Monitor {

	void dump(Map<String, String> parameters);

	long getInterval();

}
