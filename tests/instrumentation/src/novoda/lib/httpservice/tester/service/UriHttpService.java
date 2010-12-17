package novoda.lib.httpservice.tester.service;

import novoda.lib.httpservice.HttpQueuedService;
import novoda.lib.httpservice.handler.AsyncHandler;
import novoda.lib.httpservice.handler.BaseAsyncHandler;
import novoda.lib.httpservice.tester.util.AppLogger;

public class UriHttpService extends HttpQueuedService<String> {

	private AsyncHandler<String> handler = new BaseAsyncHandler<String>(String.class) {
		@Override
		public void onContentReceived(String content) {
			AppLogger.logVisibly("Content received : " + content);
		}

		@Override
		public void onThrowable(Throwable t) {
			AppLogger.logVisibly("There was an exception during the call : " + t);
		}
	};
	
	@Override
	protected AsyncHandler<String> getHandler() {
		return handler;
	}

}
