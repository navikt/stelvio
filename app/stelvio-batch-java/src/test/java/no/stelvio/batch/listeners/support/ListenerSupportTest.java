package no.stelvio.batch.listeners.support;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test for {@link ListenerSupport}
 * 
 * @author Jar Jar Binks (Accenture)
 * 
 */
public class ListenerSupportTest {

	private static final long LONG_CONSTANT_VALUE = 152364516;
	private static final String EXPECTED_RESULT = "42:19:24.516";

	/**
	 * Test method for
	 * {@link no.nav.common.batch.springbatch.listeners.support.ListenerSupport#formatMillisecondsDurationAsHumanReadableString(long)}
	 * .
	 */
	@Test
	public void testFormatMillisecondsDurationAsHumanReadableString() {

		String result = ListenerSupport.formatMillisecondsDurationAsHumanReadableString(LONG_CONSTANT_VALUE);

		Assert.assertEquals(EXPECTED_RESULT, result);
	}

}