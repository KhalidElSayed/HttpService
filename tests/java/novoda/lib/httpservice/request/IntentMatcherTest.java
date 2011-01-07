package novoda.lib.httpservice.request;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import novoda.lib.httpservice.util.CustomRobolectricTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

@RunWith(CustomRobolectricTestRunner.class)
public class IntentMatcherTest {
	
	@Test
	public void shouldMatchByUidReturnTrueIfAreTheSameIntent() {
		Intent intent = new IntentRequestBuilder("test", "http://www.google.com").build();
		
		assertTrue(IntentMatcher.matchByUid(intent, intent));
	}
	
	@Test
	public void shouldMatchByUidReturnTrueIfAreTheSameIntentWithMock() {
		Intent intent1 = mock(Intent.class);
		when(intent1.getLongExtra(Request.Extra.uid, 0L)).thenReturn(12L);
		Intent intent2 = mock(Intent.class);
		when(intent2.getLongExtra(Request.Extra.uid, 0L)).thenReturn(12L);
		
		assertTrue(IntentMatcher.matchByUid(intent1, intent2));
	}
	
	@Test
	public void shouldMatchByUidReturnFalseIfIntentsHaveDifferentUids() {
		Intent intent1 = mock(Intent.class);
		when(intent1.getLongExtra(Request.Extra.uid, 0L)).thenReturn(222L);
		Intent intent2 = mock(Intent.class);
		when(intent2.getLongExtra(Request.Extra.uid, 0L)).thenReturn(111L);
		
		assertFalse(IntentMatcher.matchByUid(intent1, intent2));
	}

}
