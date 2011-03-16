package com.novoda.lib.httpservice.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.novoda.lib.httpservice.exception.FileNotFinished;

public class FileReader {

	private static final long THRESHOLD = 1024l*1024l;
	private static final int DEFAULT_BUFFER_SIZE = 8*1024;
	
	private long threshold;
	
	public FileReader() {
		setThreshold(THRESHOLD);
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}
	
	public void addToFile(String filepath, InputStream is) throws FileNotFinished {
		try {
			long offset = 0l;
			File file = new File(filepath);
			FileOutputStream os = new FileOutputStream(file);
			
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			long count = 0;
			int n = 0;
			is.skip(offset);
			os.getChannel();
			while (-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
				count += n;
				if(count > threshold) {
					os.flush();
					os.close();
					is.close();
					throw new FileNotFinished(file.getAbsolutePath());
				}
			}
			os.close();
			is.close();
		} catch (FileNotFoundException e) {
			Log.e("problem reading content", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			Log.e("problem reading content", e);
			throw new RuntimeException(e);
		}
	}
	
	public boolean exists(String filepath) {
		File file = new File(filepath);
		return file.exists();
	}

}
