package com.novoda.lib.httpservice.tester.service;

import android.content.Intent;

import com.novoda.lib.httpservice.HttpService;
import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.ActorFactory;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.controller.ContextHttpWrapper;
import com.novoda.lib.httpservice.storage.Storage;
import com.novoda.lib.httpservice.tester.util.Log;

public class AppService extends HttpService {
	
	public static final String TYPE1 = "vnd.android.cursor.dir/vnd.httptester.actor1";
	public static final String TYPE2 = "vnd.android.cursor.dir/vnd.httptester.actor2";
	
	// This is just a sample to show how to implement a 
	// switcher on the actors
	// Check the manifest intent-filters
	@Override
	protected ActorFactory getActorFactory() {
		return new ActorFactory() {
			@Override
			public Actor getActor(Intent intent, Storage storage) {
				Actor actor = new LoggingActor(intent, storage);
				if(TYPE1.equals(intent.getType())) {
					Log.v("Actor1 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else if(TYPE2.equals(intent.getType())) {
					Log.v("Actor2 defined for intent : " + intent.toString());
					actor = new LoggingActor(intent, storage);
				} else {
					Log.v("No actor defined for intent : " + intent.toString() + ", " + intent.getType());
					actor =  new LoggingActor(intent, storage);
				}
				actor.applayContext(new ContextHttpWrapper(AppService.this.getApplicationContext()));
				return actor;
			}
		};
	}

}
