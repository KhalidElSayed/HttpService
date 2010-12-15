package novoda.lib.httpservice.handler;


public interface AsyncHandler<T> {

	void onStatusReceived(String status);

    void onHeadersReceived(String headers);

    void onThrowable(Throwable t);

	void onContentReceived(T content);

	Class<?> getContentClass();
    
}
