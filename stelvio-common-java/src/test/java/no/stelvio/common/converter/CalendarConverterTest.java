package no.stelvio.common.converter;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import org.apache.commons.beanutils.ConversionException;

/**
 * Test class for CalendarConverter.
 * 
 */
public class CalendarConverterTest extends TestCase {

	private CalendarConverter converter = null;

	/**
	 * Setup tests.
	 * 
	 * @throws Exception exception
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		converter = new CalendarConverter();
	}

	/**
	 * Test null input.
	 * 
	 */
	public void testConvertNull() {
		assertTrue(converter.convert(String.class, null) == null);
	}

	/**
	 * Test Calendar input.
	 * 
	 */
	public void testConvertCal() {
		assertTrue(converter.convert(String.class, Calendar.getInstance()) instanceof Calendar);
	}

	/**
	 * Test Date input.
	 * 
	 */
	public void testConvertDate() {
		assertTrue(converter.convert(Date.class, new Date()) instanceof Calendar);
	}

	/**
	 * Test Illegal input.
	 * 
	 */
	public void testConvertOther() {
		try {
			converter.convert(String.class, "abc");
			fail();
		} catch (ConversionException ce) {
			assertTrue(true);
		}
	}
}
