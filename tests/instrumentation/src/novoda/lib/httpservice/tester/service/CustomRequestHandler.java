package novoda.lib.httpservice.tester.service;

import static novoda.lib.httpservice.tester.util.Log.d;
import novoda.lib.httpservice.SimpleRequestHandler;
import novoda.lib.httpservice.provider.IntentWrapper;
import novoda.lib.httpservice.provider.Response;

public class CustomRequestHandler extends SimpleRequestHandler {
	
	private static final String PATH = "/";
	
	@Override
	public boolean match(IntentWrapper intentWrapper) {
		if(PATH.equals(intentWrapper.getUri().getPath())) {
			d("do match!");
			return true;
		}
		d("doesn't match!");
		return false;
	}
	
	@Override
	public void onContentReceived(IntentWrapper intentWrapper, Response response) {
		d("Received content for request handler : " + response.getContentAsString().length());
	}

}
