package novoda.lib.httpservice;

import android.content.Intent;


public class Request {
	
	private static final String PROTOCOL_INDICATOR = "://";

	private static final String DEFAULT_PROTOCOL = "http" + PROTOCOL_INDICATOR;
	
	private String url;
	
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

	public Request(Intent intent) {
		// TODO Auto-generated constructor stub
	}

	public String getUrl() {
		return url;
	}

	
}
