package com.novoda.httpservice.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import com.novoda.httpservice.provider.Response;
import com.novoda.httpservice.test.IOUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@Ignore
@RunWith(RobolectricTestRunner.class)
public class ResponseTest {
	
	@Test
	public void shouldEntityUtilsReturnNullBecauseShadowImplIsMissing() throws IllegalStateException, IOException {
		String expectedContent = "pippo";
		HttpEntity entity = mock(HttpEntity.class);
		InputStream expectedContentAsStream = IOUtils.getInputStream(expectedContent);
		when(entity.getContent()).thenReturn(expectedContentAsStream);
		when(entity.getContentLength()).thenReturn(new Long(-1));
		when(entity.getContentType()).thenReturn(null);
		
		assertNotNull("If this is not working the roboletric has some change in the EntityUtils", EntityUtils.toString(entity));
	}
	
	@Test
	public void shouldConvertTheContentIntoString() throws IllegalStateException, IOException{
		String expectedContent = "pippo";
		
		HttpResponse httpResponse = mock(HttpResponse.class);
		
		HttpEntity entity = mock(HttpEntity.class);
		InputStream expectedContentAsStream = IOUtils.getInputStream(expectedContent);
		when(entity.getContent()).thenReturn(expectedContentAsStream);
		when(entity.getContentLength()).thenReturn(new Long(-1));
		when(entity.getContentType()).thenReturn(null);

		when(httpResponse.getEntity()).thenReturn(entity);
		
		Response response = new Response();
		response.setHttpResponse(httpResponse);
		
		assertEquals("pippo", response.getContentAsString());
	}
	
	@Test
	public void shouldConvertTheContentIntoStringReturnEmptyWhenContentIsNull() throws IllegalStateException, IOException{
		HttpResponse httpResponse = mock(HttpResponse.class);

		HttpEntity entity = mock(HttpEntity.class);
		when(entity.getContent()).thenReturn(null);

		when(httpResponse.getEntity()).thenReturn(entity);
		
		Response response = new Response();
		response.setHttpResponse(httpResponse);
		
		assertEquals("", response.getContentAsString());
	}

}
