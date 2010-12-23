package novoda.lib.httpservice.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;
import android.net.Uri;
import android.os.ResultReceiver;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestWriterTest {
	
	private static final String URL = "http://www.google.com";
	
	@Test
	public void shouldBuildUpGetRequestFromUri() {
		Uri uri = Uri.parse(URL);
		Intent intent = new RequestWriter(uri).write();
		assertNotNull(intent);
		String action = intent.getAction();
		assertNotNull(action);
		assertEquals(Request.Action.request, action);
	}
	
	@Test
	public void shouldBuildUpGetRequestFromUrl() {
		Intent intent = new RequestWriter(URL).write();
		assertNotNull(intent);
		String action = intent.getAction();
		assertNotNull(action);
		assertEquals(Request.Action.request, action);
		Uri uri = intent.getData();
		assertNotNull(uri);
	}
	
	@Test
	public void shouldAttachResultReceiver() {
		ResultReceiver expectedReceiver = mock(ResultReceiver.class);
		Intent intent = new RequestWriter(URL).attach(expectedReceiver).write();
		ResultReceiver actualReceiver = intent.getParcelableExtra(Request.Extra.result_receiver);
		assertNotNull(actualReceiver);
		assertEquals(expectedReceiver, actualReceiver);
	}
	
	@Test
	public void shouldBuildGetByDefault() {
		Intent intent = new RequestWriter(URL).write();
		int method = intent.getIntExtra(Request.Extra.method, Request.Method.GET);
		assertEquals(Request.Method.GET, method);
	}
	
	@Test
	public void shouldBuildPost() {
		Intent intent = new RequestWriter(URL).asPost().write();
		int method = intent.getIntExtra(Request.Extra.method, Request.Method.GET);
		assertEquals(Request.Method.POST, method);
	}
	
	@Test
	public void shouldWriteHandlerInformationOnIntent() {
		Intent intent = new RequestWriter(URL).handlerKey("specificHandler").write();
		String handlerKey = intent.getStringExtra(Request.Extra.handler_key);
		assertEquals("specificHandler", handlerKey);
	}
	
	@Ignore //robolectric doesn't support getParcalableArrayList TODO fix this
	@Test
	public void shouldWriteParcellOfParameters() {
		HashMap<String,String> expectedParams = new HashMap<String,String>();
		expectedParams.put("key", "XXYY");
		expectedParams.put("something", "somevalue");
		Intent intent = new RequestWriter(URL).params(expectedParams).write();
		ArrayList<ParcelableBasicNameValuePair> actualParams = intent.getParcelableArrayListExtra(Request.Extra.params);
		assertNotNull(actualParams);
		assertEquals(2, actualParams.size());
		assertEquals("", actualParams.get(0).getName());
		assertEquals("", actualParams.get(0).getValue());
		assertEquals("", actualParams.get(1).getName());
		assertEquals("", actualParams.get(1).getValue());
	}

}
