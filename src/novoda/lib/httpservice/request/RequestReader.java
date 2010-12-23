package novoda.lib.httpservice.request;

import static novoda.lib.httpservice.util.LogTag.Core.d;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;

import java.util.List;

import novoda.lib.httpservice.exception.RequestException;
import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

public class RequestReader {

	protected RequestReader() {
	}

	public static final Request read(Intent intent) {
		String action = intent.getAction();
		if (action == null) {
			throw new RequestException("Can't build a request with a null action");
		}
		if (Request.Action.request.equals(action)) {
			Request request = new Request();
			readUri(intent, request);
			readMethod(intent, request);
			readHandlerKey(intent, request);
			readResultReceiver(intent, request);
			readParams(intent, request);
			return request;
		} else {
			throw new RequestException("Action : " + action  + " is not implemented");
		}
	}

	private static void readHandlerKey(Intent intent, Request request) {
		String handlerKey = intent.getStringExtra(Request.Extra.handler_key);
		if(handlerKey != null) {
			request.setHandlerKey(handlerKey);
		}
	}

	private static void readResultReceiver(Intent intent, Request request) {
		Object receiverObj = intent.getParcelableExtra(Request.Extra.result_receiver);
		if (receiverObj == null) {
			if (debugIsEnable()) {
				d("Request receiver is null!");
			}
			return;
		}
		if(receiverObj instanceof ResultReceiver) {
			ResultReceiver resultReceiver = (ResultReceiver)receiverObj;
				if (debugIsEnable()) {
					d("Building request for intent with request receiver");
				}
				request.setResultReceiver(resultReceiver);
		} else {
			throw new RequestException("Problem generating reading the result receiver");
		}
	}

	private static void readUri(Intent intent, Request request) {
		Uri uri = intent.getData();
		if(uri == null) {
			throw new RequestException("Request url and uri are not specified. Need at least one!");
		}
		request.setUri(uri);
	}

	private static void readMethod(Intent intent, Request request) {
		int method = intent.getIntExtra(Request.Extra.method, Request.Method.GET);
		request.setMethod(method);
	}
	
	private static void readParams(Intent intent, Request request) {
		List<ParcelableBasicNameValuePair> params = intent.getParcelableArrayListExtra(Request.Extra.params);
	    request.setParams(params);
	}

	// StringBuilder query = new StringBuilder(URLEncodedUtils.format(
	// params, "UTF-8"));
	// if (uri.getQuery() != null && uri.getQuery().length() > 3) {
	// if (params.size() > 0)
	// query.append('&');
	// query.append(uri.getQuery());
	// }
//	request.setUri(URIUtils.createURI(uri.getScheme(), uri.getHost(),
//			uri.getPort(), uri.getEncodedPath(), uri.getQuery(), uri
//					.getFragment()));

}
