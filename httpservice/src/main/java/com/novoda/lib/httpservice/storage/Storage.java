package com.novoda.lib.httpservice.storage;

import android.content.Context;
import android.content.Intent;

public interface Storage {
	
	void queued(Context context, Intent intent);
	
	void contendReceived(Context context, Intent intet);
	
	void contendConsumed(Context context, Intent intet);

}
