package novoda.lib.httpservice.request;

import java.net.URISyntaxException;

import novoda.lib.httpservice.HttpServiceAction;

import org.apache.http.client.utils.URIUtils;

import android.content.Intent;
import android.net.Uri;

public class RequestBuilder {

	protected RequestBuilder() {
	}

	public static final Request build(Intent intent) {
		String action = intent.getAction();
		if (action == null) {
			throw new RequestException(
					"Can't build a request with a null action");
		}
		if (HttpServiceAction.simple_request.equals(action)) {
			return buildRequestFromSimpleRequestIntent(intent);
		} else if (HttpServiceAction.uri_request.equals(action)) {
			return buildRequestFromUriRequestIntent(intent);
		} else if (HttpServiceAction.parcable_request.equals(action)) {
			return buildRequestFromParcableRequestIntent(intent);
		} else {
			throw new RequestException("Action : " + action
					+ " is not implemented");
		}
	}

	private static final Request buildRequestFromSimpleRequestIntent(
			Intent intent) {
		String url = intent.getStringExtra(HttpServiceAction.Extra.url);
		return new Request(url);
	}

	private static final Request buildRequestFromUriRequestIntent(Intent intent) {
		Uri uri = intent.getData();
		if (uri == null) {
			throw new RequestException("Uri is null, can't procede anymore");
		}
		Request request = new Request();

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
			throw new RequestException(
					"Problem generating the request from the uri : "
							+ e.getMessage());
		}
	}

	private static final Request buildRequestFromParcableRequestIntent(
			Intent intent) {
		RequestParcable parcable = intent
				.getParcelableExtra(HttpServiceAction.Extra.request_parcable);
		if (parcable == null) {
			throw new RequestException(
					"Parcable is null, can't procede anymore");
		}
		Request request = new Request();
		return request;
	}

}
