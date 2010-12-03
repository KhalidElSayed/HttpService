
package novoda.lib.httpservice.net;

import novoda.rest.concurrent.RequestHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class AsyncHttpClient {

    private final HttpClient client;

    public AsyncHttpClient(HttpClient client) {
        this.client = client;
    }

    public <T> Future<T> execute(HttpUriRequest request, RequestHandler<T> handler) {
        return new TestFuture<T>(new AsyncRequest<T>(request, handler));
    }

    class TestFuture<T> extends FutureTask<T> {
        public TestFuture(Callable<T> callable) {
            super(callable);
        }
    }

    private class AsyncRequest<T> implements Callable<T> {

        private final HttpUriRequest request;

        private final RequestHandler<T> handler;

        public AsyncRequest(HttpUriRequest request, RequestHandler<T> handler) {
            this.request = request;
            this.handler = handler;
        }

        @Override
        public T call() throws Exception {
            return client.execute(request, handler);
        }
    }
}
