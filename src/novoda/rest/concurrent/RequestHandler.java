
package novoda.rest.concurrent;

import java.util.concurrent.Callable;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;

public interface RequestHandler<T> extends ResponseHandler<T>, Callable<T> {

    public void onThrowable(Throwable e);

    public void onTimeout();

    public void onPreCall(HttpUriRequest request);

    public void onPostCall(T data);

    public void onFinish();

    public void onUnauthorized(int httpCode);
}
