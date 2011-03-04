package com.novoda.lib.httpservice.actor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.novoda.lib.httpservice.controller.ContextHttpWrapper;

import android.content.Context;
import android.content.Intent;

public class Actor {

	private Intent intent;
	
	private ContextHttpWrapper context;
	
	public Actor(Intent intent) {
		this.intent = intent;
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
    
    protected void onCreate() {
    	
    }
   
	protected void onResume() {
       
    }
    
	protected void onPause() {
	
	}
	
    protected void onStop() {

    }
    
    protected void onDestroy() {
    	
    }
    
    protected void onLowMemory() {
    	
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
		// TODO Auto-generated method stub
		
	}

}
