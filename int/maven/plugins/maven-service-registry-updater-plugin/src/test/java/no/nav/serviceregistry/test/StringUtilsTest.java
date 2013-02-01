package no.nav.serviceregistry.test;

import static no.nav.serviceregistry.util.StringUtils.empty;
import static no.nav.serviceregistry.util.StringUtils.notEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testEmpty() {
		assertTrue(empty(""));
		assertTrue(empty((String[])null));
		assertTrue(empty(new String()));
		assertTrue(empty("", null, ""));
		assertFalse(empty("sl"));
		assertFalse(empty("", "s", ""));
		assertFalse(empty("s", "", "s"));
	}

	@Test
	public void testNotEmpty() {
		assertFalse(notEmpty(""));
		assertFalse(notEmpty((String[])null));
		assertFalse(notEmpty(new String()));
		assertFalse(notEmpty("", null, ""));
		assertFalse(notEmpty("s", "", "s"));
		assertTrue(notEmpty("s"));
		assertTrue(notEmpty("s", "s", "s"));
	}

}
