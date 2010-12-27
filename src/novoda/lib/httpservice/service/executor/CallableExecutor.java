package novoda.lib.httpservice.service.executor;

import java.util.concurrent.Callable;

import android.content.Intent;

public interface CallableExecutor<T> {

    Callable<T> getCallable(Intent intent);
    
}
