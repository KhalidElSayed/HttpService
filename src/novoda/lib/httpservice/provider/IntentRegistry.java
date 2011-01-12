package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.Log.v;
import static novoda.lib.httpservice.util.Log.verboseLoggingEnabled;

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
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > isAlreadyInQueue : " + intentWrapper);
		}
		for(IntentWrapper r : registry.keySet()) {
			if(r.sameAs(intentWrapper)) {
				if(verboseLoggingEnabled()) {
					v("IntentRegistry > Attaching intent with another intent");
				}
				registry.get(r).add(intentWrapper);					
				return true;
			}
		}
		registry.put(intentWrapper, new ArrayList<IntentWrapper>());
		return false;
	}
	
	public boolean isRecentlyBeenConsumed(IntentWrapper intentWrapper) {
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > isRecentlyBeenConsumed : " + intentWrapper);
		}
		for(IntentWrapper r : recentlyConsumed.keySet()) {
			if(r.sameAs(intentWrapper)) {
				Long time = recentlyConsumed.get(intentWrapper);
				if(intentWrapper.isCacheDisabled()) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > removing intent from cache");
					}
					recentlyConsumed.remove(intentWrapper);
				} else if(System.currentTimeMillis() - time.longValue() < CACHE_TIME) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > intent is cached");
					}
					return true;
				} else {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > removing intent from cache");
					}
					recentlyConsumed.remove(intentWrapper);
				}
			}
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
		recentlyConsumed.put(intentWrapper, System.currentTimeMillis());
	}
	
}
