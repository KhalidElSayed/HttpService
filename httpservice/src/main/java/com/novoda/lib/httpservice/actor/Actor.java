package com.novoda.lib.httpservice.actor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.storage.Storage;

import android.content.Context;
import android.content.Intent;

public class Actor {

	private Intent intent;
	
	private ContextHttpWrapper context;
	
	private Storage storage;
	
	public Actor(Intent intent, Storage storage) {
		this.intent = intent;
		this.setStorage(storage);
	}
	
	public void applayContext(ContextHttpWrapper context) {
		this.context = context;
	}
	
	public Context getHttpContext() {
		return context;
	}
	
    public Intent getIntent() {
        return intent;
    }
     
    public void setIntent(Intent newIntent) {
        intent = newIntent;
    }
    
	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public Storage getStorage() {
		return storage;
	}
    
    public void onCreate() {
    	storage.queued(intent);
    }
   
    public void onResume() {
       
    }
    
    public void onPause() {
	
	}
	
    public void onStop() {

    }
    
    public void onDestroy() {
    	storage.contendConsumed(intent);
    }
    
    public void onLowMemory() {
    	
    }

	public void onPreprocess(HttpUriRequest method, HttpContext context2) {
		// TODO Auto-generated method stub
		
	}

	public void onPostprocess(HttpResponse httpResponse, HttpContext context2) {
		// TODO Auto-generated method stub
		
	}

	public void onThrowable(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	public void onResponseReceived(HttpResponse httpResponse) {
		storage.contendReceived(intent);
	}

	public boolean onResponseError(int statusCode) {
		// TODO Auto-generated method stub		
		return false;
	}

}
