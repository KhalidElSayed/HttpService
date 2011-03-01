package com.novoda.lib.httpservice.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Intent;

/**
 * This test class contains a bunch of test to make sure a certain thing in
 * robolectric works or not
 * 
 * @author luigi@novoda.com
 *
 */
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
