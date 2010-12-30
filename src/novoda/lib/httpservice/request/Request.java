package novoda.lib.httpservice.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import novoda.lib.httpservice.exception.RequestException;

import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;

import android.net.Uri;
import android.os.ResultReceiver;

/**
 * This class contains all the information necessary to the provider
 * to execute the http method.
 * 
 * @author luigi@novoda.com
 *
 */
public class Request {
	
	public static final String SIMPLE_BUNDLE_RESULT = "result";
	
	private static final String ENCODING = "UTF-8";
	
	private static final char AND = '&';
	
	private static final String EMPTY = "";
	
	public static interface Extra {
		String result_receiver = "novoda.lib.httpservice.extra.RESULT_RECEIVER";
		String method = "novoda.lib.httpservice.extra.METHOD";
		String handler_key = "novoda.lib.httpservice.extra.HANDLER_KEY";
		String params = "novoda.lib.httpservice.extra.PARAMS";		
	}

	public static interface Method {
		int GET = 0;
		int POST = 1;	
	} 
	
	private String handlerKey;
	
	private Uri uri;
	
	private int method;
	
	private ResultReceiver resultReceiver;
	
	private List<ParcelableBasicNameValuePair> params;
	
	public Request() {
	}
	
	public Request(String url) {
		this(Uri.parse(url));
	}
	
	public Request(Uri uri) {
		setUri(uri);
		setMethod(Method.GET);
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public Uri getUri() {
		return uri;
	}

	public ResultReceiver getResultReceiver() {
		return resultReceiver;
	}

	public void setResultReceiver(ResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public String getHandlerKey() {
		return handlerKey;
	}
	
	public boolean isGet() {
		if(Method.GET == getMethod()) {
			return true;
		}
		return false;
	}

	public boolean isPost() {
		if(Method.POST == getMethod()) {
			return true;
		}
		return false;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public int getMethod() {
		return method;
	}

	public void setHandlerKey(String handlerKey) {
		this.handlerKey = handlerKey;
	}

	public void setParams(List<ParcelableBasicNameValuePair> params) {
		this.params = params;
	}

	public List<ParcelableBasicNameValuePair> getParams() {
		return params;
	}
	
	public static final URI asURI(Uri uri, List<ParcelableBasicNameValuePair> params) {
		StringBuilder query = new StringBuilder(EMPTY);
		if(params != null) {
			query.append(URLEncodedUtils.format(params, ENCODING));
	        if (uri.getQuery() != null && uri.getQuery().length() > 3) {
	            if (params.size() > 0) {
	                query.append(AND);
	            }
	            query.append(uri.getQuery());
	        }
        }
        return asURI(uri, query.toString());
    }
	
	public static final URI asURI(Uri uri, String query) {
        try {
            return URIUtils.createURI(uri.getScheme(), uri.getHost(), uri.getPort(),
                    uri.getEncodedPath(), query, uri.getFragment());
        } catch (URISyntaxException e) {
            throw new RequestException("Problem generating the URI with " + uri);
        }
    }
	
	public static final URI asURI(Uri uri) {
        return asURI(uri, EMPTY);
    }
	
	public static final URI asURI(Request request) {		
		return asURI(request.getUri(), request.getParams());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Request with URI: ");
		sb.append(uri).append(" and ").append(" requestReceiver: ");
		if(resultReceiver != null) {
			sb.append(" is not null");
		} else {
			sb.append(" is null");
		}
		sb.append(" and ").append("handlerKey: ").append(handlerKey);
		sb.append(" and ").append("method: ").append(method);
		return sb.toString();
	}
	
}
