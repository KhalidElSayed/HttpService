package com.novoda.lib.httpservice.storage;

import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.utils.IntentReader;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

public class DatabaseStorage implements Storage {

	private Context context;
	
	public DatabaseStorage(Context context) {
		this.context = context;
	}
	
	@Override
	public void contendConsumed(Intent intent) {
		ContentValues cv = asValues(intent, false);
		cv.put(IntentModel.Column.status, IntentModel.Status.consumed);
		context.getContentResolver().insert(IntentModel.URI, cv);
	}

	@Override
	public void contendReceived(Intent intent) {
		ContentValues cv = asValues(intent, false);
		cv.put(IntentModel.Column.status, IntentModel.Status.received);
		context.getContentResolver().insert(IntentModel.URI, cv);
	}

	@Override
	public void queued(Intent intent) {
		ContentValues cv = asValues(intent, true);
		cv.put(IntentModel.Column.status, IntentModel.Status.queued);
		context.getContentResolver().insert(IntentModel.URI, cv);
	}
	
	private ContentValues asValues(Intent intent, boolean forInsert) {
		ContentValues cv = new ContentValues();
		IntentReader reader = new IntentReader(intent);
		cv.put(IntentModel.Column.id, intent.filterHashCode());
		if(forInsert) {
			cv.put(IntentModel.Column.created, System.currentTimeMillis());	
			cv.put(IntentModel.Column.uri, reader.asURI().toString());
		}
		cv.put(IntentModel.Column.modified, System.currentTimeMillis());
		return cv;
	}
	

}
