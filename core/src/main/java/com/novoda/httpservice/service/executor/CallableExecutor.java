package com.novoda.httpservice.service.executor;

import java.util.concurrent.Callable;

import com.novoda.httpservice.provider.IntentWrapper;

public interface CallableExecutor<T> {

    Callable<T> getCallable(IntentWrapper request);
    
}
