package com.novoda.lib.httpservice.storage;

import android.content.Intent;

public interface Storage {
	
	void queued(Intent intent);
	
	void contendReceived(Intent intet);
	
	void contendConsumed(Intent intet);

}
