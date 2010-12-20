package novoda.lib.httpservice.request;

import java.net.URI;


public class Request {
	
	private static final String PROTOCOL_INDICATOR = "://";

	private static final String DEFAULT_PROTOCOL = "http" + PROTOCOL_INDICATOR;
	
	private String url;
	
	private URI uri;
	
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
	
}
