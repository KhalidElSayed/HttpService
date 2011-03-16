package com.novoda.lib.httpservice.tester.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;

import com.novoda.lib.httpservice.HttpService;
import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.actor.ZipActor;
import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.exception.FileNotFinished;
import com.novoda.lib.httpservice.storage.DatabaseStorage;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.storage.provider.DatabaseManager.IntentModel;
import com.novoda.lib.httpservice.tester.util.Log;

public class AppService extends HttpService {
	
	public static final String TYPE1 = "vnd.android.cursor.dir/vnd.httptester.actor1";
	public static final String TYPE2 = "vnd.android.cursor.dir/vnd.httptester.actor2";
	public static final String ZIP = "vnd.android.cursor.dir/vnd.httptester.zip";
	
	@Override
	protected Storage getStorage() {
		return new DatabaseStorage();
	}
	
	// This is just a sample to show how to implement a 
	// switcher on the actors
	// Check the manifest intent-filters
	@Override
	protected ActorFactory getActorFactory() {
		Log.dev("getting actor factory");
		return new ActorFactory() {
			@Override
			public Actor getActor(Intent intent, Storage storage) {
				Actor actor = null;
				Log.dev("intent type : " + intent.getType());
				if(TYPE1.equals(intent.getType())) {
					Log.v("Actor1 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else if(TYPE2.equals(intent.getType())) {
					Log.v("Actor2 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else if(ZIP.equals(intent.getType())) {
					Log.v("Zip defined for intent : " + intent.toString());
					actor = new ZipActor(intent, storage) {
						
						@Override
						public void onResponseReceived(HttpResponse httpResponse) {
							onResponseReceivedCallback(httpResponse, getIntent(), getStorage());
						}
						
						@Override
						protected void onResponseErrorCallback(int errorCode, Intent intent, Storage storage) {
							Log.dev("Actor1 defined for intent : ");
							
						}
						@Override
						protected void onThrowableCallback(Throwable t, Intent intent, Storage storage) {
							Log.dev("throwable");
						}
						@Override
						protected void onResponseReceivedCallback(HttpResponse res, Intent intent, Storage storage) {
							Log.dev("Actor1 defined for intent : ");
							tryToHandleResponse(res, intent, storage);
						}
						
						protected void tryToHandleResponse(HttpResponse res, Intent intent, Storage storage) {
							Context context = getApplicationContext();
							try {
								Log.dev("checking for intent : " + intent.filterHashCode());
								ContentValues cv = storage.getIntent(context, intent);
								Log.dev("intent has : " + cv);
								File file = getFile(context, cv);
								long offset = 0l;
								if(file == null) {
									Log.dev("file is null");
									storage.queued(context, intent);
									file = createFile(intent);
								} else {
									Log.dev("file is not null");
									offset = getOffsetFromExistingFile(file);
									Log.dev("lenght is : " + offset);
								}
								storage.contendReceived(context, intent);
								readResponseFrom(offset, res, file);
								//storage.contendConsumed(context, intent);							
							} catch(FileNotFinished fnf) {
								String filename = fnf.getFilename();
								storage.updateDownload(context, intent, filename);
								context.startService(intent);
							}
						}
						
						@Override
						public void onLowMemory() {
							Log.dev("on log memory");
						}
					};
				} else {
					Log.v("No actor defined for intent : " + intent.toString() + ", " + intent.getType());
					actor =  new LoggingActor(intent, storage);
				}
				actor.applayContext(new ContextHttpWrapper(AppService.this.getApplicationContext()));
				return actor;
			}
		};
	}
	
	private static final long THRESHOLD = 1024l*1024l;
	private static final int DEFAULT_BUFFER_SIZE = 8*1024;
	
	private void readResponseFrom(long offset, HttpResponse res, File file) throws FileNotFinished {
		Log.dev("readResponseFrom");
		try {
			FileOutputStream os = new FileOutputStream(file);
			HttpEntity entity = res.getEntity();
			InputStream is = entity.getContent();
			
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			long count = 0;
			int n = 0;
			Log.dev("starting from offset : " + offset);
			is.skip(offset);
			os.getChannel();
			while (-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
				count += n;
				if(count > THRESHOLD) {
					Log.dev("should stop to start another intent");
					os.flush();
					os.close();
					is.close();
					//entity.consumeContent();
					throw new FileNotFinished(file.getAbsolutePath());
				}
				//Log.v("reading... " + count);
			}
			Log.dev("yea !");
			os.close();
			is.close();
			//entity.consumeContent();
		} catch (FileNotFoundException e) {
			Log.e("problem reading content", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			Log.e("problem reading content", e);
			throw new RuntimeException(e);
		}
	}
	
	private long getOffsetFromExistingFile(File file) {
		return file.length();
	}
	
	private File getFile(Context c, ContentValues cv) {
		if(cv == null) {
			return null;
		}
		String filename = cv.getAsString(IntentModel.Column.filename);
		if(TextUtils.isEmpty(filename)) {
			return null;
		}
		return new File(filename);
	}
	
	private File createFile(Intent intent) {
		String filename = "download-" + intent.filterHashCode() + ".zip";
		String path = Environment.getExternalStorageDirectory() + "/" + filename;
		return new File(path);
	}
	
	@Override
	public void onLowMemory() {
		Log.dev("onLowMemory on AppService");
		super.onLowMemory();
	}
	
}
