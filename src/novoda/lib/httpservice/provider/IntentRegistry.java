package novoda.lib.httpservice.provider;

import static novoda.lib.httpservice.util.Log.Registry.v;
import static novoda.lib.httpservice.util.Log.Registry.verboseLoggingEnabled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Registry to keep attach together similar intent
 * 
 * @author luigi
 *
 */
public class IntentRegistry {	
	
	private static final long CACHE_TIME = 1000*5;
	private Map<String,List<IntentWrapper>> registry = Collections.synchronizedMap(new HashMap<String, List<IntentWrapper>>());
	private Map<String,Long> cache = Collections.synchronizedMap(new HashMap<String, Long>());

	public synchronized boolean isInQueue(IntentWrapper intentWrapper) {
		if(intentWrapper.isCacheDisabled()) {
			if(verboseLoggingEnabled()) {
				v("IntentRegistry > is already in the queue, but cached is disabled : " + intentWrapper);
				v("IntentRegistry > Queue is : ");
				dump(registry.keySet());
			}
			return false;
		} else {
			for(String r : registry.keySet()) {
				if(r.equals(intentWrapper.asURI().toString())) {
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
		registry.put(intentWrapper.asURI().toString(), new ArrayList<IntentWrapper>());
		return false;
	}
	
	private void dump(Set<String> keySet) {
		for(String intent : keySet) {
			v("IntentRegistry > Intent Wrapper " + intent);
		}
	}

	public synchronized boolean isInCache(IntentWrapper intentWrapper) {
		for(String r : cache.keySet()) {
			if(r.equals(intentWrapper.asURI().toString())) {
				if(verboseLoggingEnabled()) {
					v("IntentRegistry > is recently been consumed : " + intentWrapper);
					v("IntentRegistry > Cache is : ");
					dump(cache.keySet());
				}
				Long time = cache.get(intentWrapper);
				if(intentWrapper.isCacheDisabled()) {
					if(verboseLoggingEnabled()) {
						v("IntentRegistry > removing intent from cache, is forced!");
					}
					removeFromCache(intentWrapper);
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
					removeFromCache(intentWrapper);
					return false;
				}
			}
		}
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > intent was not recently consumed");
		}
		return false;
	}
	
	public synchronized List<IntentWrapper> getSimilarIntents(IntentWrapper intentWrapper) {
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > Gettting similar intents");
		}
		List<IntentWrapper> intents = registry.get(intentWrapper);
		onConsumed(intentWrapper);
		return intents;
	}
	
	public synchronized void onConsumed(IntentWrapper intentWrapper) {
		if(verboseLoggingEnabled()) {
			v("IntentRegistry > intent consumed");
		}
		removeFromRegistry(intentWrapper);
		cache.put(intentWrapper.asURI().toString(), System.currentTimeMillis());
	}
	
	private synchronized void removeFromRegistry(IntentWrapper intentWrapper) {
		String toRemove = null;
		for(String iw : registry.keySet()) {
			iw.equals(intentWrapper.asURI().toString());
			toRemove = iw;
		}
		if(toRemove != null) {
			registry.remove(toRemove);
		}
	}
	
	private synchronized void removeFromCache(IntentWrapper intentWrapper) {
		String toRemove = null;
		for(String iw : cache.keySet()) {
			iw.equals(intentWrapper.asURI().toString());
			toRemove = iw;
		}
		if(toRemove != null) {
			registry.remove(toRemove);
		}
	}
	
}
