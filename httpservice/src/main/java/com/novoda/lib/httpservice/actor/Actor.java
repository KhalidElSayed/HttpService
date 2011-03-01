package com.novoda.lib.httpservice.actor;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Actor {

	private Intent intent;
	
	private Application application;
	private ContextHttpWrapper context;
	
	public Actor() {
		
	}
	
	public void applayContext(ContextHttpWrapper context) {
		this.context = context;
	}
	
	public Context getHttpContext() {
		return context;
	}
	
    /** 
     * Return the intent that started this activity. 
     */
    public Intent getIntent() {
        return intent;
    }
    
    /** 
     * Change the intent returned by {@link #getIntent}.  This holds a 
     * reference to the given intent; it does not copy it.  Often used in 
     * conjunction with {@link #onNewIntent}. 
     *  
     * @param newIntent The new Intent object to return from getIntent 
     * 
     * @see #getIntent
     * @see #onNewIntent
     */ 
    public void setIntent(Intent newIntent) {
        intent = newIntent;
    }
    
    /** 
     * Return the application that owns this activity.
     */
    public final Application getApplication() {
        return application;
    }
    
    public void startActorForResult(Intent intent, int requestCode) {
    	
    }
    
    public void startActor(Intent intent) {
    	
    }
	
    protected void onCreate(Bundle savedInstanceState) {

    }
    
    protected void onResume() {
       
    }

    protected void onPostResume() {

    }
    
	protected void onPause() {
	
	}
	
    protected void onStop() {

    }
    
    protected void onDestroy() {
    	
    }
    
    protected void onLowMemory() {
    	
    }
    
}
