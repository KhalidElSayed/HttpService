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
public class IntentRequestBuilder {
	
	private Intent intent;
	
	public IntentRequestBuilder(String url)  {
		this(Uri.parse(url));
	}
	
	public IntentRequestBuilder(Uri uri)  {
		intent = new Intent(Request.Action.request, uri);
	}
	
	public IntentRequestBuilder asPost() {
		return method(Request.Method.POST);
	}

	private IntentRequestBuilder method(int method) {
		intent.putExtra(Request.Extra.method, method);
		return this;
	}

	public IntentRequestBuilder attach(ResultReceiver resultReceiver) {
		intent.putExtra(Request.Extra.result_receiver, resultReceiver);
		return this;
	}
	
	public IntentRequestBuilder withHandlerKey(String handlerKey) {
		intent.putExtra(Request.Extra.handler_key, handlerKey);
		return this;
	}

	public IntentRequestBuilder withParams(Map<String, String> params) {
		ArrayList<ParcelableBasicNameValuePair> parcelables = new ArrayList<ParcelableBasicNameValuePair>();
		for(Entry<String,String> param : params.entrySet()) {
			parcelables.add(new ParcelableBasicNameValuePair(param.getKey(), param.getValue()));
		}
		return withParams(parcelables);
	}
	
	public IntentRequestBuilder withParams(ArrayList<ParcelableBasicNameValuePair> params) {
		intent.putParcelableArrayListExtra(Request.Extra.params, params);
		return this;
	}

	public Intent build() {
		return intent;
	}

}
