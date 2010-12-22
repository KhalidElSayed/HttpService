package novoda.lib.httpservice.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import novoda.lib.httpservice.HttpServiceConstant;

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
		assertEquals(HttpServiceConstant.request, action);
	}
	
	@Test
	public void shouldBuildUpGetRequestFromUrl() {
		Intent intent = new RequestWriter(URL).write();
		assertNotNull(intent);
		String action = intent.getAction();
		assertNotNull(action);
		assertEquals(HttpServiceConstant.request, action);
		String url = intent.getStringExtra(HttpServiceConstant.Extra.url);
		assertNotNull(url);
		assertEquals(URL, url);
	}
	
	@Test
	public void shouldAttachResultReceiver() {
		ResultReceiver expectedReceiver = mock(ResultReceiver.class);
		Intent intent = new RequestWriter(URL).attach(expectedReceiver).write();
		ResultReceiver actualReceiver = intent.getParcelableExtra(HttpServiceConstant.Extra.result_receiver);
		assertNotNull(actualReceiver);
		assertEquals(expectedReceiver, actualReceiver);
	}
	
	@Test
	public void shouldBuildGetByDefault() {
		Intent intent = new RequestWriter(URL).write();
		int method = intent.getIntExtra(HttpServiceConstant.Extra.method, Request.Method.GET);
		assertEquals(Request.Method.GET, method);
	}
	
	@Test
	public void shouldBuildPost() {
		Intent intent = new RequestWriter(URL).method(Request.Method.POST).write();
		int method = intent.getIntExtra(HttpServiceConstant.Extra.method, Request.Method.GET);
		assertEquals(Request.Method.POST, method);
	}
	
	@Ignore
	@Test
	public void shouldAddParcellOfParametersForPost() {
		//TODO something I know I have to do
	}

}
