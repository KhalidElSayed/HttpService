
package com.novoda.lib.httpservice.processor;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;

import java.io.IOException;

public class BasicOAuthProcessor implements HttpProcessor {

    @Override
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
    }

    @Override
    public void process(HttpResponse response, HttpContext context) throws HttpException,
            IOException {
    }

}
