package novoda.lib.httpservice.request;

import java.net.URI;

import novoda.lib.httpservice.exception.RequestException;

import android.os.ResultReceiver;


public class Request {
	
	public static final String SIMPLE_BUNDLE_RESULT = "result";
	
	private static final String PROTOCOL_INDICATOR = "://";

	private static final String DEFAULT_PROTOCOL = "http" + PROTOCOL_INDICATOR;
	
	private String url;
	
	private URI uri;
	
	private ResultReceiver resultReceiver;
	
	private String contentClassSimpleName;
	
	public Request() {
	}
	
	public Request(String url) {
		if(url == null) {
			throw new RequestException("Url is null!");
		}
		if(url.contains(PROTOCOL_INDICATOR)) {
			this.url = url;			
		} else {
			this.url = DEFAULT_PROTOCOL + url;
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public URI getUri() {
		return uri;
	}

	public ResultReceiver getResultReceiver() {
		return resultReceiver;
	}

	public void setResultReceiver(ResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Request with url : ");
		sb.append(url).append(" and ").append(" URI : ");
		sb.append(uri).append(" and ").append(" requestReceiver : ");
		if(resultReceiver != null) {
			sb.append(" is not null");
		} else {
			sb.append(" is null");
		}
		return sb.toString();
	}

	public void setContentClassSimpleName(String contentClassSimpleName) {
		this.contentClassSimpleName = contentClassSimpleName;
	}

	public String getContentClassSimpleName() {
		return contentClassSimpleName;
	}

	public String getHandlerKey() {
		// TODO Auto-generated method stub
		return null;
	}
}
