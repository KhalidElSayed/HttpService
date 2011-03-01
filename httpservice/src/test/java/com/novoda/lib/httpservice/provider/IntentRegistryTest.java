package com.novoda.lib.httpservice.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import com.novoda.lib.httpservice.provider.IntentRegistry;
import com.novoda.lib.httpservice.provider.IntentWrapper;
import com.novoda.lib.httpservice.test.Time;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class IntentRegistryTest {
	
	private IntentRegistry registry;
	
	@Before
	public void setup() {
		registry = new IntentRegistry();
		registry.isInQueue(getNoiseRequest());
	}
	
	@Ignore
	@Test
	public void shouldNotSkipRequestIfIsTheFirst() {
		IntentWrapper r1 = getMockRequest();
		boolean toSkip = registry.isInQueue(r1);
		assertFalse(toSkip);
	}
	
	@Ignore
	@Test
	public void shouldSkipRequestIfSimilarRequestWasMade() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		assertTrue(r1.sameAs(r2));
		registry.isInQueue(r1);
		
		boolean toSkip = registry.isInQueue(r2);
		assertTrue(toSkip);
	}
	
	@Ignore
	@Test
	public void shouldGetSimilarRequest() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		registry.isInQueue(r1);
		registry.isInQueue(r2);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(1, intents.size());
		assertEquals(r2, intents.get(0));
	}
	
	@Ignore
	@Test
	public void shouldSimilarRequests() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		IntentWrapper r3 = getMockRequest();
		registry.isInQueue(r1);
		registry.isInQueue(r2);
		registry.isInQueue(r3);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(2, intents.size());
		assertEquals(r2, intents.get(0));
		assertEquals(r3, intents.get(1));
	}
	
	@Ignore
	@Test
	public void shouldCleanUpInqueueIntentAfterGetSimilar() {
		IntentWrapper r1 = getMockRequest();
		IntentWrapper r2 = getMockRequest();
		registry.isInQueue(r1);
		registry.isInQueue(r2);
		
		List<IntentWrapper> intents = registry.getSimilarIntents(r1);
		assertEquals(1, intents.size());
		assertEquals(r2, intents.get(0));
		
		assertFalse(registry.isInQueue(r1));
	}
	
	@Ignore
	@Test
	public void shouldKeepAListOfRecentlyConsumedIntent() {
		IntentWrapper r1 = getMockRequest();
		registry.onConsumed(r1);
		assertTrue(registry.isInCache(r1));
	}
	
	@Ignore
	@Test
	public void shouldKeepAListOfRecentlyConsumedIntentExpiredAfter5Secs() {
		IntentWrapper r1 = getMockRequest();
		registry.onConsumed(r1);
		Time.await(5001);
		assertFalse(registry.isInCache(r1));
	}
	
	@Ignore
	@Test
	public void shouldSkipCacheIfIntentIsCacheDisabled() {
		IntentWrapper r1 = getMockRequest();
		when(r1.isCacheDisabled()).thenReturn(true);
		registry.onConsumed(r1);
		assertFalse(registry.isInCache(r1));
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
