package com.novoda.lib.httpservice.config;

import com.novoda.lib.httpservice.actor.Actor;
import com.novoda.lib.httpservice.storage.Storage;

import android.content.Intent;

public interface Config {

	Actor getActor(Intent intent, Storage storage);

}
