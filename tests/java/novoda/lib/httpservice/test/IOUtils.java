package novoda.lib.httpservice.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class IOUtils {
	
	public static final InputStream getInputStream(String content) {
		try {
			return new ByteArrayInputStream(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Problem generating input stream");
		}
	}

}
