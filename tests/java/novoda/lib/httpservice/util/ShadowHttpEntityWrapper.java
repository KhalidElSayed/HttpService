package novoda.lib.httpservice.util;

import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

import com.xtremelabs.robolectric.util.Implements;

@Implements(HttpEntityWrapper.class)
public class ShadowHttpEntityWrapper {
	
	public void __constructor__(HttpEntity wrapped) {
    
    }

	
	
}
