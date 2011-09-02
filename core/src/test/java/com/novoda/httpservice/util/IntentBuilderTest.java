package com.novoda.httpservice.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;

import com.novoda.httpservice.provider.IntentWrapper;
import com.novoda.httpservice.util.IntentBuilder;
import com.novoda.httpservice.util.ParcelableBasicNameValuePair;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

@Ignore
@RunWith(CustomRobolectricTestRunner.class)
public class IntentBuilderTest {
	
	private static final String URL = "http://www.google.com";
	private static final String ACTION = "request";
	
	@Test
	public void shouldBuildUpGetRequestFromUri() {
		Uri uri = Uri.parse(URL);
		Intent intent = new IntentBuilder(ACTION, uri).build();
		assertNotNull(intent);
		String action = intent.getAction();
		assertNotNull(action);
		assertEquals(ACTION, action);
	}
	
	@Test
	public void shouldBuildUpGetRequestFromUrl() {
		Intent intent = new IntentBuilder(ACTION, URL).build();
		assertNotNull(intent);
		String action = intent.getAction();
		assertNotNull(action);
		assertEquals(ACTION, action);
		Uri uri = intent.getData();
		assertNotNull(uri);
	}
	
	@Test
	public void shouldAttachResultReceiver() {
		ResultReceiver expectedReceiver = mock(ResultReceiver.class);
		Intent intent = new IntentBuilder(ACTION, URL).withStringResultReceiver(expectedReceiver).build();
		ResultReceiver actualReceiver = intent.getParcelableExtra(IntentWrapper.Extra.result_receiver);
		assertNotNull(actualReceiver);
		assertEquals(expectedReceiver, actualReceiver);
	}
	
	@Test
	public void shouldBuildGetByDefault() {
		Intent intent = new IntentBuilder(ACTION, URL).build();
		int method = intent.getIntExtra(IntentWrapper.Extra.method, IntentWrapper.Method.GET);
		assertEquals(IntentWrapper.Method.GET, method);
	}
	
	@Test
	public void shouldBuildPost() {
		Intent intent = new IntentBuilder(ACTION, URL).asPost().build();
		int method = intent.getIntExtra(IntentWrapper.Extra.method, IntentWrapper.Method.GET);
		assertEquals(IntentWrapper.Method.POST, method);
	}
	
	@Test
	public void shouldWriteHandlerInformationOnIntent() {
		Intent intent = new IntentBuilder(ACTION, URL).withHandlerKey("specificHandler").build();
		String handlerKey = intent.getStringExtra(IntentWrapper.Extra.handler_key);
		assertEquals("specificHandler", handlerKey);
	}
	
	@Test
	public void shouldIntentHaveId() {
		Intent intent = new IntentBuilder(ACTION, URL).build();
		assertNotNull(intent);
		long uid = intent.getLongExtra(IntentWrapper.Extra.uid, 0l);
		assertTrue(0l != uid);
	}
	
	@Test
	public void shouldIntentBeCacheDisabled() {
		Intent intent = new IntentBuilder(ACTION, URL).withDisableCache().build();
		assertNotNull(intent);
		long uid = intent.getLongExtra(IntentWrapper.Extra.uid, 0l);
		assertTrue(0l != uid);
	}
	
	@Ignore("robolectric doesn't support getParcalableArrayList TODO fix this")
	@Test
	public void shouldWriteParcellOfParameters() {
		HashMap<String,String> expectedParams = new HashMap<String,String>();
		expectedParams.put("key", "XXYY");
		expectedParams.put("something", "somevalue");
		Intent intent = new IntentBuilder(ACTION, URL).withParams(expectedParams).build();
		ArrayList<ParcelableBasicNameValuePair> actualParams = intent.getParcelableArrayListExtra(IntentWrapper.Extra.params);
		assertNotNull(actualParams);
		assertEquals(2, actualParams.size());
		assertEquals("", actualParams.get(0).getName());
		assertEquals("", actualParams.get(0).getValue());
		assertEquals("", actualParams.get(1).getName());
		assertEquals("", actualParams.get(1).getValue());
	}

}
