package novoda.lib.httpservice.request;

import static novoda.lib.httpservice.util.LogTag.Core.debugIsEnable;
import static novoda.lib.httpservice.util.LogTag.Core.debug;

import java.net.URISyntaxException;

import novoda.lib.httpservice.HttpServiceConstant;
import novoda.lib.httpservice.exception.RequestException;

import org.apache.http.client.utils.URIUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

public class RequestBuilder {

	protected RequestBuilder() {
	}

	public static final Request build(Intent intent) {
		String action = intent.getAction();
		if (action == null) {
			throw new RequestException("Can't build a request with a null action");
		}
		if (HttpServiceConstant.simple_request.equals(action)) {
			return buildRequestFromSimpleRequestIntent(intent);
		} else if (HttpServiceConstant.uri_request.equals(action)) {
			return buildRequestFromUriRequestIntent(intent);
		} else {
			throw new RequestException("Action : " + action  + " is not implemented");
		}
	}

	private static final Request buildRequestFromSimpleRequestIntent(Intent intent) {
		String url = intent.getStringExtra(HttpServiceConstant.Extra.url);
		Request request = new Request(url);
		//TODO need to generalize
		request.setContentClassSimpleName(String.class.getSimpleName());
		request.setResultReceiver(getResultReceiver(intent));
		if (debugIsEnable()) {
			debug("Building request for intent : " + request.toString());
		}
		return request;
	}

	private static final Request buildRequestFromUriRequestIntent(Intent intent) {
		Uri uri = intent.getData();
		if (uri == null) {
			throw new RequestException("Uri is null, can't procede anymore");
		}
		Request request = new Request();
		//TODO need to generalize
		request.setContentClassSimpleName(String.class.getSimpleName());
		request.setResultReceiver(getResultReceiver(intent));
		try {
			// StringBuilder query = new StringBuilder(URLEncodedUtils.format(
			// params, "UTF-8"));
			// if (uri.getQuery() != null && uri.getQuery().length() > 3) {
			// if (params.size() > 0)
			// query.append('&');
			// query.append(uri.getQuery());
			// }
			request.setUri(URIUtils.createURI(uri.getScheme(), uri.getHost(),
					uri.getPort(), uri.getEncodedPath(), uri.getQuery(), uri
							.getFragment()));
			return request;
		} catch (URISyntaxException e) {
			throw new RequestException("Problem generating the request from the uri : " + e.getMessage());
		}
	}

	private static final ResultReceiver getResultReceiver(Intent intent) {
		Object rr = intent.getParcelableExtra(HttpServiceConstant.Extra.request_parcable);
		if(rr instanceof ResultReceiver) {
			ResultReceiver rr1 = (ResultReceiver)rr;
			if (rr1 == null) {
				if (debugIsEnable()) {
					debug("Request receiver is null!");
				}
				return null;
			} else {
				if (debugIsEnable()) {
					debug("Building request for intent with request receiver");
				}
				return rr1;
			}
		} else {
			throw new RequestException("Problem generating reading the result receiver");
		}
	}

}
