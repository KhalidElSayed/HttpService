package novoda.lib.httpservice.handler;



public class BaseAsyncHandler<T> implements AsyncHandler<T> {

	private Class<?> contentClass;
	
	public BaseAsyncHandler(Class<?> contentClass) {
		this.contentClass = contentClass;
	}
	
	@Override
	public Class<?> getContentClass() {
		return contentClass;
	}
	
	@Override
	public void onThrowable(Throwable t) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void onContentReceived(T content) {
		//TODO
	}


	@Override
	public void onHeadersReceived(String headers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusReceived(String status) {
		// TODO Auto-generated method stub
		
	}

}
