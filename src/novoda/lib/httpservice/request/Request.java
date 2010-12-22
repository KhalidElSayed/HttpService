package novoda.lib.httpservice.request;

import android.net.Uri;
import android.os.ResultReceiver;

public class Request {
	
	public interface Method {
		int GET = 0;
		int POST = 1;	
	} 
	
	public static final String SIMPLE_BUNDLE_RESULT = "result";
	
	private String url;
	
	private Uri uri;
	
	private int method;
	
	private ResultReceiver resultReceiver;
	
	private String contentClassSimpleName;
	
	public Request() {
	}
	
	public Request(String url) {
		setUrl(url);
		setMethod(Method.GET);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
}
