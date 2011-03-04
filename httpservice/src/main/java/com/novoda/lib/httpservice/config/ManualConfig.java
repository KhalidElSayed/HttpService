package com.novoda.lib.httpservice.config;

import android.content.Intent;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.actor.LoggingActor;
import com.novoda.lib.httpservice.storage.Storage;

public class ManualConfig implements Config {

	@Override
	public Actor getActor(Intent intent, Storage storage) {
		return new LoggingActor(intent);
	}

}
