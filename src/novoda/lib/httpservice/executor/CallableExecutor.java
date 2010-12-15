package novoda.lib.httpservice.executor;

import java.util.concurrent.Callable;

import android.content.Intent;

public interface CallableExecutor<T> {

    Callable<T> getCallable(Intent intent);
    
}
