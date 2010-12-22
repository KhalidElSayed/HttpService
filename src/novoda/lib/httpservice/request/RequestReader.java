package novoda.lib.httpservice.request;

import static novoda.lib.httpservice.util.LogTag.Core.debug;
import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import novoda.lib.httpservice.HttpServiceConstant;
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
		if (HttpServiceConstant.request.equals(action)) {
			Request request = new Request();
			readMethod(intent, request);
			boolean uri = readUri(intent, request);
			boolean url = readUrl(intent, request);
			if(!uri && !url) {
				throw new RequestException("Request url and uri are not specified. Need at least one!");
			}
			readResultReceiver(intent, request);
			return request;
		} else {
			throw new RequestException("Action : " + action  + " is not implemented");
		}
	}

	private static void readResultReceiver(Intent intent, Request request) {
		Object receiverObj = intent.getParcelableExtra(HttpServiceConstant.Extra.result_receiver);
		if (receiverObj == null) {
			if (debugIsEnable()) {
				debug("Request receiver is null!");
			}
			return;
		}
		if(receiverObj instanceof ResultReceiver) {
			ResultReceiver resultReceiver = (ResultReceiver)receiverObj;
				if (debugIsEnable()) {
					debug("Building request for intent with request receiver");
				}
				request.setResultReceiver(resultReceiver);
		} else {
			throw new RequestException("Problem generating reading the result receiver");
		}
	}

	private static boolean readUrl(Intent intent, Request request) {
		String url = intent.getStringExtra(HttpServiceConstant.Extra.url);
		if(url != null) {
			request.setUrl(url);
			return true;
		}
		return false;
	}

	private static boolean readUri(Intent intent, Request request) {
		Uri uri = intent.getData();
		if(uri != null) {
			request.setUri(uri);
			return true;
		}
		return false;
	}

	private static void readMethod(Intent intent, Request request) {
		int method = intent.getIntExtra(HttpServiceConstant.Extra.method, Request.Method.GET);
		request.setMethod(method);
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
