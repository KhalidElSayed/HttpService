package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.Log.Registry.v;
import static novoda.lib.httpservice.util.Log.Registry.verboseLoggingEnabled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry to keep attach together similar intent
 * 
 * @author luigi
 *
 */
public class IntentRegistry {	
	
	private static final long CACHE_TIME = 1000*5;
	
	private Map<IntentWrapper,List<IntentWrapper>> registry = Collections.synchronizedMap(new HashMap<IntentWrapper, List<IntentWrapper>>());
	private Map<IntentWrapper,Long> recentlyConsumed = Collections.synchronizedMap(new HashMap<IntentWrapper, Long>());

	public boolean isAlreadyInQueue(IntentWrapper intentWrapper) {
		if(intentWrapper.isCacheDisabled()) {
			if(verboseLoggingEnabled()) {
				v("IntentRegistry > is already in the queue, but cached is disabled : " + intentWrapper);
			}
			return false;
		} else {
			for(IntentWrapper r : registry.keySet()) {
				if(r.sameAs(intentWrapper)) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > is already in the queue, attaching intent with another intent : " + intentWrapper);
					}
					registry.get(r).add(intentWrapper);
					return true;
				}
			}
			if(verboseLoggingEnabled()) {
				v("IntentRegistry > is not in the queue : " + intentWrapper);
			}
		}
		//registry.put(intentWrapper, new ArrayList<IntentWrapper>());
		return false;
	}
	
	public boolean isRecentlyBeenConsumed(IntentWrapper intentWrapper) {
		for(IntentWrapper r : recentlyConsumed.keySet()) {
			if(r.sameAs(intentWrapper)) {
				if(verboseLoggingEnabled()) {
					v("IntentRegistry > is recently been consumed : " + intentWrapper);
				}
				Long time = recentlyConsumed.get(intentWrapper);
				if(intentWrapper.isCacheDisabled()) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > removing intent from cache, is forced!");
					}
					recentlyConsumed.remove(intentWrapper);
					return false;
				} else if(time != null && System.currentTimeMillis() - time.longValue() < CACHE_TIME) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > intent is cached");
					}
					return true;
				} else {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > removing intent from cache");
					}
					recentlyConsumed.remove(intentWrapper);
					return false;
				}
			}
		}
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > intent was not recently consumed");
		}
		return false;
	}
	
	public List<IntentWrapper> getSimilarIntents(IntentWrapper intentWrapper) {
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > Gettting similar intents");
		}
		List<IntentWrapper> intents = registry.get(intentWrapper);
		onConsumed(intentWrapper);
		return intents;
	}
	
	public void onConsumed(IntentWrapper intentWrapper) {
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > intent consumed");
		}
		registry.remove(intentWrapper);
		//recentlyConsumed.put(intentWrapper, System.currentTimeMillis());
	}
	
}
