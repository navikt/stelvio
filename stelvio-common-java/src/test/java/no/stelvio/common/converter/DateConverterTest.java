package no.stelvio.common.converter;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import org.apache.commons.beanutils.ConversionException;

/**
 * Test class for DateConverter.
 * 
 */
public class DateConverterTest extends TestCase {

	private DateConverter converter = null;

	/**
	 * Test convert.
	 */
	public void testConvert() {
	}

	/**
	 * Setup test.
	 * 
	 * @throws Exception
	 *             exception
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		converter = new DateConverter();
	}

	/**
	 * Test null input.
	 * 
	 */
	public void testConvertNull() {
		assertTrue(converter.convert(String.class, null) == null);
	}

	/**
	 * Test Date input.
	 * 
	 */
	public void testConvertDate() {
		assertTrue(converter.convert(Date.class, new Date()) instanceof Date);
	}

	/**
	 * Test Calendar input.
	 * 
	 */
	public void testConvertCalendar() {
		assertTrue(converter.convert(Calendar.class, Calendar.getInstance()) instanceof Date);
	}

	/**
	 * Test String input.
	 * 
	 */
	public void testConvertString() {
		assertTrue(converter.convert(String.class, "07.03.2005") instanceof Date);
	}

	/**
	 * Test bogus input.
	 * 
	 */
	public void testConvertBogus() {
		try {
			converter.convert(String.class, "abc");
			fail();
		} catch (ConversionException ce) {
			assertTrue(true);
		}

	}

}
