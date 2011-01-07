package novoda.lib.httpservice.util;

import java.util.HashMap;

import android.content.Intent;

import com.xtremelabs.robolectric.shadows.ShadowIntent;
import com.xtremelabs.robolectric.util.Implementation;
import com.xtremelabs.robolectric.util.Implements;

@Implements(Intent.class)
public class CustomShadowIntent extends ShadowIntent {

	private HashMap<String, Long> longs = new HashMap<String, Long>();
	
	@Implementation
    public Intent putExtra(String key, long value) {
		longs.put(key, value);
        return super.putExtra(key, value);
    }
	
	@Implementation
    public long getLongExtra(String name, long defaultValue) {
        Long foundValue = (Long) longs.get(name);
        return foundValue == null ? defaultValue : foundValue;
    }
	
}
