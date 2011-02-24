package novoda.lib.httpservice.tester.service;

import static novoda.lib.httpservice.tester.util.Log.d;

import java.io.IOException;

import novoda.lib.httpservice.processor.Processor;
import novoda.lib.httpservice.provider.IntentWrapper;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public class CustomProcessor implements Processor {
	
	@Override
	public boolean match(IntentWrapper intentWrapper) {
		return true;
	}

	@Override
	public void process(HttpRequest response, HttpContext context) throws HttpException, IOException {
		d("Processing request 2...");
	}

	@Override
	public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		d("Processing response 2...");	
	}

}
