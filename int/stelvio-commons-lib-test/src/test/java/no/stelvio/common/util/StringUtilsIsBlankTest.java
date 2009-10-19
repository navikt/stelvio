package no.stelvio.common.util;

import junit.framework.Assert;

import org.junit.Test;

public class StringUtilsIsBlankTest {
	@Test
	public void testNull() {
		Assert.assertTrue(StringUtils.isBlank(null));
	}
	
	@Test
	public void testEmpty() {
		Assert.assertTrue(StringUtils.isBlank(""));
	}
	
	@Test
	public void testBlank() {
		Assert.assertTrue(StringUtils.isBlank("       "));
	}
	
	@Test
	public void testDefault() {
		Assert.assertFalse(StringUtils.isBlank("foo"));
	}
}
