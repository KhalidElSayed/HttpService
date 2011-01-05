
package novoda.lib.httpservice.request;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

/**
 * This class is responsible to help in setting all the necessary request
 * information on the intent so that the ReqeustReader can on the other side
 * extract the request object from the intent. A simple intent can be build like
 * : new RequestWriter(URI).build() -> is a simple get on the specified uri
 * 
 * @author luigi@novoda.com
 */
public class IntentRequestBuilder {

    private Intent intent;

    private ArrayList<ParcelableBasicNameValuePair> params = new ArrayList<ParcelableBasicNameValuePair>();

    public IntentRequestBuilder(String action, String url) {
        this(action, Uri.parse(url));
    }

    public IntentRequestBuilder(String action, Uri uri) {
        intent = new Intent(action, uri);
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

    public IntentRequestBuilder withParams(Map<String, String> parameters) {
        for (Entry<String, String> entry : parameters.entrySet()) {
            this.params.add(new ParcelableBasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this;
    }

    public IntentRequestBuilder withParam(String key, String value) {
        params.add(new ParcelableBasicNameValuePair(key, value));
        return this;
    }

    public IntentRequestBuilder withParams(ArrayList<ParcelableBasicNameValuePair> params) {
        params.addAll(params);
        return this;
    }

    public IntentRequestBuilder withResultReceiver(ResultReceiver receiver) {
        intent.putExtra(Request.Extra.result_receiver, receiver);
        return this;
    }
    
    public Intent build() {
        intent.putParcelableArrayListExtra(Request.Extra.params, params);
        return intent;
    }
}
