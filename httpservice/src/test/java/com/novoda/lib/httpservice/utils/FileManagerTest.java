package com.novoda.lib.httpservice.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.Test;

public class FileManagerTest {

	private static final String FILE_PATH = "filepath";
	private static final String ENCODING = "UTF-8";
	private static final String CONTENT = "1234567890A1234567890B1234567890";

	@Test
	public void shouldSaveToFile() {
		InputStream stream = getInputStream(CONTENT);

		FileManager.addToFile(FILE_PATH, stream, 2);

		//TODO
	}
	
	@Test
	public void shouldReadFromExistingFile() {
		InputStream stream = getInputStream(CONTENT);

		FileManager.addToFile(FILE_PATH, stream, 2);

		//TODO
	}
	
	@Test
	public void shouldAddToExistingFile() {
		InputStream stream = getInputStream(CONTENT);

		FileManager.addToFile(FILE_PATH, stream, 2);
		FileManager.addToFile(FILE_PATH, stream, 2);
		FileManager.addToFile(FILE_PATH, stream, 2);
		
		//TODO
		
		assertFile(CONTENT);
	}

	private void assertFile(String content2) {
		File file = new File(FILE_PATH);
		
	}

	public InputStream getInputStream(String convert) {
		try {
			return (InputStream) new ByteArrayInputStream(convert.getBytes(ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is, ENCODING));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

}
