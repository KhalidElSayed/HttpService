
package com.novoda.lib.httpservice.processor;

import com.novoda.lib.httpservice.utils.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;

import java.io.IOException;

public class LoggableProcessor implements HttpProcessor {

    public LoggableProcessor() {
        Log.i("=============== Start LoggableProcessor =========== ");
    }
    
    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Log.i("=============== Start LoggableProcessor =========== ");
        Log.i("Resquest: " + request.getRequestLine().toString());
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
        Log.i("Received response:");
        Log.i("StatusLine:" + response.getStatusLine());
        Log.i("=============== End LoggableProcessor =========== ");
    }

}
