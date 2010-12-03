
package novoda.rest.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.protocol.HttpContext;

public class GZipResponseInterceptor implements HttpResponseInterceptor {
    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
        if (response == null) {
            throw new IOException("response can not be null");
        }
        final HttpEntity entity = response.getEntity();
        final Header header = entity.getContentEncoding();
        if (header != null) {
            for (HeaderElement h : header.getElements()) {
                if (h.getName().equalsIgnoreCase("gzip")) {
                    response.setEntity(new GZipEntity(response.getEntity()));
                    return;
                }
            }
        }
    }

    public class GZipEntity extends HttpEntityWrapper {
        public GZipEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }
    }
}
