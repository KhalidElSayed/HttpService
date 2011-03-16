package com.novoda.lib.httpservice.actor;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.novoda.lib.httpservice.exception.FileNotFinished;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.utils.FileReader;
import com.novoda.lib.httpservice.utils.Log;

public class ZipActor extends LoggingActor {

	public ZipActor(Intent intent, Storage storage) {
		super(intent, storage);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onResponseReceived(HttpResponse httpResponse) {
		tryToHandleResponse(httpResponse, getIntent(), getStorage());
	}
	
	protected void tryToHandleResponse(HttpResponse res, Intent intent, Storage storage) {
		Context context = getHttpContext();
		try {
			Log.v("checking for intent : " + intent.filterHashCode());
			ContentValues cv = storage.getIntent(context, intent);
			Log.v("intent has : " + cv);
			
			String filepath = getFilepath(context, cv, intent);
			Log.v("filePath : " + filepath);
			FileReader reader = new FileReader();
			reader.setBufferSize(1);
			reader.setThreshold(1);
			
			if(!reader.exists(filepath)) {
				storage.queued(context, intent);									
			}
			storage.contendReceived(context, intent);
			readResponseFrom(reader, res, filepath);					
		} catch(FileNotFinished fnf) {
			String filename = fnf.getFilename();
			storage.updateDownload(context, intent, filename);
			context.startService(intent);
		}
	}
	
	private void readResponseFrom(FileReader reader, HttpResponse res, String filen) throws FileNotFinished {
		HttpEntity entity = res.getEntity();
		try {
			InputStream is = entity.getContent();
			reader.addToFile(filen, is);
		} catch (IOException io) {
			throw new RuntimeException();
		}
	}
	
	private String getFilepath(Context c, ContentValues cv, Intent intent) {
		String existing = getFileName(c, cv);
		if(existing == null) {
			String filename = "download-" + intent.filterHashCode() + ".zip";
			String path = Environment.getExternalStorageDirectory() + "/" + filename;
			return path;			
		}
		return existing;
	}
	
	private String getFileName(Context c, ContentValues cv) {
		if(cv == null) {
			return null;
		}
		String filename = cv.getAsString(IntentModel.Column.filename);
		if(TextUtils.isEmpty(filename)) {
			return null;
		}
		return filename;
	}
	
	
	
}
