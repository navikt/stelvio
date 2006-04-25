package no.trygdeetaten.integration.framework.hibernate.formater;

import java.util.Date;

import junit.framework.TestCase;

import no.trygdeetaten.common.framework.error.SystemException;
import no.trygdeetaten.common.framework.util.DateUtil;

/**
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class DateFormaterTest extends TestCase {
	private DateFormater formater = new DateFormater();

	/**
	 * Test Null input
	 */
	public void testFormatInputNull() {
		assertEquals( "",formater.formatInput( null ) );
	}
	/**
	 * Test input data
	 */
	public void testFormatInput() {
		Date theDate = DateUtil.parse("09.03.2005");
		assertEquals( "20050309",formater.formatInput( theDate ) );
	}
	
	/**
	 * Test Null input
	 */
	public void testFormatOutputNull() {
		assertNull( formater.formatOutput( null) );
	}
	
	/**
	 * Test that exception is thrown on bad input.
	 */
	public void testFormatOutputEx() {
		try {
			formater.formatOutput( "abc");
			fail();
		} catch(SystemException se) {
			assertTrue(true);
		}
	}
	
	/**
	 * Test input data
	 */
	public void testFormatOutput() {
		Date theDate = DateUtil.parse("09.03.2005");
		assertEquals( theDate,formater.formatOutput( "20050309" ) );
	}

	/**
	 * Setup tests.
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		formater.setFormatPattern("yyyyMMdd");
	}
}
