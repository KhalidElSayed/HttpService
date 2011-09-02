package com.novoda.httpservice.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

@Ignore
@RunWith(CustomRobolectricTestRunner.class)
public class ShadowsTest {
	
	@Test
	public void ensureShadowSystemWorks() {
		assertNotNull(System.nanoTime());
	}
	
	@Test
	public void ensureShadowIntentWorks() {
		Intent intent = new Intent();
		intent.putExtra("test", 1L);
		assertEquals(1L, intent.getLongExtra("test", 0L));
	}
	
}
