package com.novoda.httpservice.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import android.content.Intent;

import com.novoda.httpservice.provider.IntentWrapper;

public class IntentMatcherTest {
	
	@Test
	public void shouldMatchByUidReturnTrueIfAreTheSameIntent() {
	    Intent intent = mock(Intent.class);
	    when(intent.getLongExtra(IntentWrapper.Extra.uid, 0L)).thenReturn(12L);

		assertTrue(IntentMatcher.matchByUid(intent, intent));
	}
	
	@Test
	public void shouldMatchByUidReturnTrueIfAreTheSameIntentWithMock() {
		Intent intent1 = mock(Intent.class);
		when(intent1.getLongExtra(IntentWrapper.Extra.uid, 0L)).thenReturn(12L);
		Intent intent2 = mock(Intent.class);
		when(intent2.getLongExtra(IntentWrapper.Extra.uid, 0L)).thenReturn(12L);
		
		assertTrue(IntentMatcher.matchByUid(intent1, intent2));
	}
	
	@Test
	public void shouldMatchByUidReturnFalseIfIntentsHaveDifferentUids() {
		Intent intent1 = mock(Intent.class);
		when(intent1.getLongExtra(IntentWrapper.Extra.uid, 0L)).thenReturn(222L);
		Intent intent2 = mock(Intent.class);
		when(intent2.getLongExtra(IntentWrapper.Extra.uid, 0L)).thenReturn(111L);
		
		assertFalse(IntentMatcher.matchByUid(intent1, intent2));
	}

}
