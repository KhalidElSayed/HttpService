package novoda.lib.httpservice;

public interface HttpServiceConstant {

	String simple_request = "novoda.lib.httpservice.action.SIMPLE_REQUEST";
	
	String uri_request = "novoda.lib.httpservice.action.URI_REQUEST";

	String parcable_request = "novoda.lib.httpservice.action.PARCABLE_REQUEST";
	
	interface Extra {
		
		String url = "novoda.lib.httpservice.extra.URL";
		
		String request_parcable = "novoda.lib.httpservice.extra.REQUEST_PARCABLE";
			
	}
	
}
