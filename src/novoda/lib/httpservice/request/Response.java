package novoda.lib.httpservice.request;

import static novoda.lib.httpservice.util.LogTag.Core.e;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import novoda.lib.httpservice.exception.RequestException;

import org.apache.http.HttpResponse;

public class Response {

	private HttpResponse httpResponse;
	
	private Request request;
	
	private InputStream content;
	
	public void setHttpResponse(HttpResponse httpResponse) {
		this.httpResponse  = httpResponse;
	}
	
	public HttpResponse getHttpResponse() {
		return this.httpResponse;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public InputStream getContent() {
		if(content != null) {
			return content;
		}
		InputStream content = null;
		try {
			content = httpResponse.getEntity().getContent();
		} catch (IllegalStateException e) {
			throw new RequestException("" + e.getMessage());
		} catch (IOException e) {
			throw new RequestException("" + e.getMessage());
		}
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}
	
	public String getContentAsString() {
		return asString(getContent());
	}
	
	//==========================================================
	// is not responsibility of this class ... should move ???
	//==========================================================
	
	private static final String ENCODING = "UTF-8";
	
	private static final int BUFFER = 8192;
	
	private static final String asString(final InputStream is) {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[BUFFER];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING), BUFFER);
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} catch (Exception e) {
				e("Convertion problem from input to string ", e);
			} finally {
				try {
					is.close();
				} catch(Exception e) {
					e("Convertion problem while closing stream ", e);	
				}
			}
			return writer.toString();
		} else {        
			return "";
		}
	}
	

}
