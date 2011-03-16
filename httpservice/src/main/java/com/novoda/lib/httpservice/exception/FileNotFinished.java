package com.novoda.lib.httpservice.exception;

public class FileNotFinished extends Exception {

	private static final long serialVersionUID = 3020427103587875876L;

	private String filename;
	
	public FileNotFinished(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}

}
