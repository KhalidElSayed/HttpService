
package novoda.lib.httpservice.processor.oauth;

import java.io.IOException;
import java.util.Map.Entry;

import novoda.lib.httpservice.processor.Processor;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.protocol.HttpContext;

import android.net.Uri;
import android.util.Log;

@SuppressWarnings("serial")
public class OAuthProcessor extends CommonsHttpOAuthConsumer implements Processor {

    private static final String TAG = OAuthProcessor.class.getSimpleName();

    public OAuthProcessor(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
    }

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {        
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (request instanceof HttpUriRequest) {
            try {
                sign(request);
//                for (final Entry<String, String> e : super
//                        .sign(((RequestWrapper) request).getOriginal()).getAllHeaders().entrySet()) {
//                    request.addHeader(e.getKey(), e.getValue());
//                }
            } catch (final OAuthMessageSignerException e) {
                Log.e(TAG, "failure " + e);
            } catch (final OAuthExpectationFailedException e) {
                Log.e(TAG, "failure " + e);
            } catch (final OAuthCommunicationException e) {
                Log.e(TAG, "failure " + e);
            }
        }
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
        // Do nothing
    }

    @Override
    public boolean match(Uri uri) {
        return true;
    }
}
