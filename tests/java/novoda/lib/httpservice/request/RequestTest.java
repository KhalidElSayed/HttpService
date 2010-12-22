package novoda.lib.httpservice.request;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class RequestTest {

	@Test
	public void shouldIsGetRespondTrueIfSettedAccondingly() {
		Request r = new Request();
		r.setMethod(Request.Method.GET);
		assertTrue(r.isGet());
		assertFalse(r.isPost());
	}
	
	@Test
	public void shouldIsPostRespondTrueIfSettedAccondingly() {
		Request r = new Request();
		r.setMethod(Request.Method.POST);
		assertTrue(r.isPost());
		assertFalse(r.isGet());
	}

}
