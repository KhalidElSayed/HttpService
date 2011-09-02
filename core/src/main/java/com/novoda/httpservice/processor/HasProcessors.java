package com.novoda.httpservice.processor;

public interface HasProcessors {
	
	void remove(Processor processor);

	void add(Processor processor);
	
}
