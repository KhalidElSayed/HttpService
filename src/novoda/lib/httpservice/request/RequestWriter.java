package novoda.lib.httpservice.request;

import novoda.lib.httpservice.HttpServiceConstant;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

/**
 * This class is responsible to help in setting all the necessary
 * request information on the intent so that the ReqeustReader 
 * can on the other side extract the request object from the intent.
 * A simple intent can be build like :
 * new RequestWriter(URI).build()  -> is a simple get on the specified uri
 * 
 * @author luigi@novoda.com
 *
 */
public class RequestWriter {
	
	private Intent intent;
	
	public RequestWriter(String url)  {
		intent = new Intent(HttpServiceConstant.request);
		intent.putExtra(HttpServiceConstant.Extra.url, url);
	}
	
	public RequestWriter(Uri uri)  {
		intent = new Intent(HttpServiceConstant.request, uri);
	}
	
	public RequestWriter method(int method) {
		intent.putExtra(HttpServiceConstant.Extra.method, method);
		return this;
	}

	public RequestWriter attach(ResultReceiver resultReceiver) {
		intent.putExtra(HttpServiceConstant.Extra.result_receiver, resultReceiver);
		return this;
	}

	public Intent write() {
		return intent;
	}


}
