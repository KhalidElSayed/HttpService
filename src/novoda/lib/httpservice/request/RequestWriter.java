package novoda.lib.httpservice.request;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

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
		this(Uri.parse(url));
	}
	
	public RequestWriter(Uri uri)  {
		intent = new Intent(Request.Action.request, uri);
	}
	
	private RequestWriter method(int method) {
		intent.putExtra(Request.Extra.method, method);
		return this;
	}
	
	public RequestWriter asPost() {
		return method(Request.Method.POST);
	}

	public RequestWriter attach(ResultReceiver resultReceiver) {
		intent.putExtra(Request.Extra.result_receiver, resultReceiver);
		return this;
	}
	
	public RequestWriter handlerKey(String handlerKey) {
		intent.putExtra(Request.Extra.handler_key, handlerKey);
		return this;
	}

	public RequestWriter params(Map<String, String> params) {
		ArrayList<ParcelableBasicNameValuePair> parcelables = new ArrayList<ParcelableBasicNameValuePair>();
		for(Entry<String,String> param : params.entrySet()) {
			parcelables.add(new ParcelableBasicNameValuePair(param.getKey(), param.getValue()));
		}
		intent.putParcelableArrayListExtra(Request.Extra.params, parcelables);
		return this;
	}

	public Intent write() {
		return intent;
	}

}
