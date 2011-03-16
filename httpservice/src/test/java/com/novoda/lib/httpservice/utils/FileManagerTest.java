package com.novoda.lib.httpservice.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.novoda.lib.httpservice.exception.FileNotFinished;

public class FileManagerTest {

	private static final String FILE_PATH = "filepath";
	private static final String ENCODING = "UTF-8";
	private static final String CONTENT = "1234567890A1234567890B1234567890";

	private FileReader reader;
	
	@Before
	public void setUp() {
		reader = new FileReader();
	}
	
	@Test
	public void shouldSaveToFile() throws FileNotFinished {
		InputStream stream = getInputStream(CONTENT);

		reader.addToFile(FILE_PATH, stream);
		
		assertFileAndCleanUp(CONTENT);
	}
	
	@Test
	public void shouldAddToExistingFile() throws FileNotFinished {
		InputStream stream = getInputStream(CONTENT);
		
		reader.addToFile(FILE_PATH, stream);		
		//TODO
		assertFileAndCleanUp(CONTENT);
	}
	
	@Test(expected = FileNotFinished.class)
	public void shouldThrowFileNotFinishedFile() throws FileNotFinished {
		InputStream stream = getInputStream(CONTENT);
		
		reader.setThreshold(4);
		reader.addToFile(FILE_PATH, stream);		
		//TODO
		assertFileAndCleanUp(CONTENT);	
	}
	
	@Test
	public void shouldSumUpStream() {
		InputStream stream = getInputStream(CONTENT);
		reader.setThreshold(4);
		try {
			reader.addToFile(FILE_PATH, stream);
		} catch (FileNotFinished fnf) {
		}
		stream = getInputStream(CONTENT);
		try {
			reader.addToFile(FILE_PATH, stream);
		} catch (FileNotFinished fnf) {
		}

		assertFileAndCleanUp(CONTENT);	
	}

	private void assertFileAndCleanUp(String content) {
		File file = new File(FILE_PATH);
		Assert.assertTrue(file.exists());
		
		Assert.assertEquals(content, covertFileToString(file));
		
		deleteFile();
	}

	public InputStream getInputStream(String convert) {
		try {
			return (InputStream) new ByteArrayInputStream(convert.getBytes(ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public String covertFileToString(File file) {
		try {
			return convertStreamToString(new FileInputStream(file));
		} catch(Exception e) {
			throw new RuntimeException("Problem reading stream");
		}
	}

	public String convertStreamToString(InputStream is) {
		try {
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
		} catch(Exception e) {
			throw new RuntimeException("Problem reading stream");
		}
	}

	private void deleteFile() {
		File file = new File(FILE_PATH);
		file.delete();
		Assert.assertFalse(file.exists());
	}
}
