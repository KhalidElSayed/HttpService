
package novoda.lib.httpservice.exception;


@SuppressWarnings("serial")
public class HttpServiceException extends RuntimeException {
    
    public static final int METHOD_MISSING = 0;
    
    public static final int URI_MALFORMED = 1;
    
    public HttpServiceException() {
        super();
    }

    public HttpServiceException(String message) {
        super(message);
    }
}
