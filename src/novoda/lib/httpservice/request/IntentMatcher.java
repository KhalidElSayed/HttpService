package novoda.lib.httpservice.request;

import android.content.Intent;

public class IntentMatcher {
	
	public static final boolean matchByUid(Intent intent1, Intent intent2) {
		long uid1 = intent1.getLongExtra(Request.Extra.uid, Request.DEFAULT_UID);
		long uid2 = intent2.getLongExtra(Request.Extra.uid, Request.DEFAULT_UID);
		return (uid1 == uid2);
	}

}
