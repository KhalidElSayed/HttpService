package novoda.lib.httpservice.processor.oauth;

import static novoda.lib.httpservice.util.Log.Processor.v;
import static novoda.lib.httpservice.util.Log.Processor.e;
import static novoda.lib.httpservice.util.Log.Processor.verboseLoggingEnabled;

import java.io.IOException;

import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.provider.IntentWrapper;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;


/**
 * Processor responsible implement oauth.
 * IMPORTANT! sign of the CommonsHttpOAuthConsumer is not thread safe and
 * as such there are two options. The first is the following implementation
 * where the CommonsHttpOAuthConsumer is created on each process call. The
 * second is to wrap the sign with a synchronized block.
 * <br>
 * By not doing this the CommonsHttpOAuthConsumer can sign different request with 
 * the same oauth_nonce parameter (that must be unique x request)
 * 
 * @author luigi@novoda.com, carl@novoda.com
 *
 */
public class OAuthProcessor implements Processor {
	
	private String consumerKey; 
	private String consumerSecret;
	private String token = "";
	private String secret = "";
	
    public OAuthProcessor(String consumerKey, String consumerSecret) {
    	this.consumerKey = consumerKey;
    	this.consumerSecret = consumerSecret;
    }

    //TODO if possible move this to the constructor
    public void setTokenWithSecret(String token, String secret) {
		this.token = token;
		this.secret = secret;
	}

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {  
    	if(verboseLoggingEnabled()) {
    		v("Executing OAuthProcessor : " + request.getRequestLine().getUri());
    	}
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (request instanceof HttpUriRequest) {
            try {            	
            	CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
            	consumer.setTokenWithSecret(token, secret);
            	consumer.sign(request);
//                for (final Entry<String, String> e : super
//                        .sign(((RequestWrapper) request).getOriginal()).getAllHeaders().entrySet()) {
//                    request.addHeader(e.getKey(), e.getValue());
//                }
            } catch (final OAuthMessageSignerException e) {
                e("OAuthMessageSignerException", e);
            } catch (final OAuthExpectationFailedException e) {
                e("OAuthExpectationFailedException", e);
            } catch (final OAuthCommunicationException e) {
                e("OAuthCommunicationException", e);
            }
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        // Do nothing
    }

    @Override
    public boolean match(IntentWrapper request) {
        return true;
    }

}
