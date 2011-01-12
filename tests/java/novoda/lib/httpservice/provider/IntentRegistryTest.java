package novoda.lib.httpservice.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import novoda.lib.httpservice.provider.IntentRegistry;
import novoda.lib.httpservice.provider.IntentWrapper;
import novoda.lib.httpservice.test.Time;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class IntentRegistryTest {
	
	private IntentRegistry registry;
	
	@Before
	public void setup() {
		registry = new IntentRegistry();
		registry.isAlreadyInQueue(getNoiseRequest());
	}
	
	@Test
	public void shouldNotSkipRequestIfIsTheFirst() {
		IntentWrapper r1 = getMockRequest();
		boolean toSkip = registry.isAlreadyInQueue(r1);
		assertFalse(toSkip);
	}
	
	@Test
	public void shouldSkipRequestIfSimilarRequestWasMade() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		assertTrue(r1.sameAs(r2));
		registry.isAlreadyInQueue(r1);
		
		boolean toSkip = registry.isAlreadyInQueue(r2);
		assertTrue(toSkip);
	}
	
	@Test
	public void shouldGetSimilarRequest() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		registry.isAlreadyInQueue(r1);
		registry.isAlreadyInQueue(r2);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(1, intents.size());
		assertEquals(r2, intents.get(0));
	}
	
	@Test
	public void shouldSimilarRequests() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		IntentWrapper r3 = getMockRequest();
		registry.isAlreadyInQueue(r1);
		registry.isAlreadyInQueue(r2);
		registry.isAlreadyInQueue(r3);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(2, intents.size());
		assertEquals(r2, intents.get(0));
		assertEquals(r3, intents.get(1));
	}
	
	@Test
	public void shouldCleanUpInqueueIntentAfterGetSimilar() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		registry.isAlreadyInQueue(r1);
		registry.isAlreadyInQueue(r2);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(1, intents.size());
		assertEquals(r2, intents.get(0));
		
		assertFalse(registry.isAlreadyInQueue(r1));
	}
	
	@Test
	public void shouldKeepAListOfRecentlyConsumedIntent() {
		IntentWrapper r1 = getMockRequest();
		registry.onConsumed(r1);
		assertTrue(registry.isRecentlyBeenConsumed(r1));
	}
	
	@Test
	public void shouldKeepAListOfRecentlyConsumedIntentExpiredAfter5Secs() {
		IntentWrapper r1 = getMockRequest();
		registry.onConsumed(r1);
		Time.await(5001);
		assertFalse(registry.isRecentlyBeenConsumed(r1));
	}
	
	@Test
	public void shouldSkipCacheIfIntentIsCacheDisabled() {
		IntentWrapper r1 = getMockRequest();
		when(r1.isCacheDisabled()).thenReturn(true);
		registry.onConsumed(r1);
		assertFalse(registry.isRecentlyBeenConsumed(r1));
	}

	private IntentWrapper getMockRequest() {
		IntentWrapper r = mock(IntentWrapper.class);
		when(r.sameAs(any(IntentWrapper.class))).thenReturn(true);
		return r;
	}

	private IntentWrapper getNoiseRequest() {
		IntentWrapper r = mock(IntentWrapper.class);
		when(r.sameAs(any(IntentWrapper.class))).thenReturn(false);
		return r;
	}
	
}
