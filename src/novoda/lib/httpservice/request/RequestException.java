package novoda.lib.httpservice.request;

public class RequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RequestException(String msg) {
		super(msg);
	}
}
