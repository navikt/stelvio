package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import com.agical.rmock.extension.junit.RMockTestCase;

/**
 * Unit test of CodesTableItem.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableItemPeriodicTest extends RMockTestCase {
	
	/**
	 * Test of constructor for CodesTableItem.
	 */
	public void testCodesTableItemEmptyConstructor() {
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic();
		
		assertEquals("Test 1: Code is not set", null, cti.getCode());
		assertEquals("Test 2: Decode is not set", null, cti.getDecode());
		assertEquals("Test 3: Fromdate is not set", null, cti.getFromDate());
		assertEquals("Test 4: Todate is not set", null, cti.getToDate());
		assertEquals("Test 5: Locale is not set", null, cti.getLocale());
		assertEquals("Test 6: Validity is not set", null, cti.getIsValid());
	}

	/**
	 * Test of constructor for CodesTableItem()
	 */
	public void testCodesTableItemConstrutor() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic("t1code1", "t1decode1", date, date, locale, Boolean.TRUE);
		
		assertEquals("Test 1: Code is set", "t1code1", cti.getCode());
		assertEquals("Test 2: Decode is set", "t1decode1", cti.getDecode());
		assertEquals("Test 3: Fromdate is set", date, cti.getFromDate());
		assertEquals("Test 4: Todate is set", date, cti.getToDate());
		assertEquals("Test 5: Locale is set", locale, cti.getLocale());
		assertEquals("Test 6: Validity is set", Boolean.TRUE, cti.getIsValid());

	}

	/**
	 * Test of CodesTableItem's getters and setters.
	 */
	public void testSettersAndGetters() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic();
		cti.setCode("t1code1");
		cti.setDecode("t1decode1");
		cti.setFromDate(date);
		cti.setToDate(date);
		cti.setLocale(locale);
		cti.setIsValid(Boolean.TRUE);
		
		assertEquals("Test 1: Code is set", "t1code1", cti.getCode());
		assertEquals("Test 2: Decode is set", "t1decode1", cti.getDecode());
		assertEquals("Test 3: FromDate is set", date, cti.getFromDate());
		assertEquals("Test 4: Todate is set", date, cti.getToDate());
		assertEquals("Test 5: Locale is set", locale, cti.getLocale());
		assertEquals("Test 6: Validity is set", Boolean.TRUE, cti.getIsValid());
	}

	/**
	 * Test toString().
	 */
	public void testToString() {
		CodesTableItemPeriodic cti = new CodesTableItemPeriodic("t1code1", "t1decode1", new Date(), new Date(), new Locale("nb", "NO"), Boolean.TRUE);
		assertNotNull("Test 1: toString is not null", cti.toString());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	public void testEqualsObject() {
	
		super.assertTrue("Test 1: CodesTableItemPeriodic.equals(CodesTableItemPeriodic) should have matched", TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP1));
		super.assertFalse("Test 2: CodesTableItemPeriodic.equals(null) should not have matched", TestCodesTableItemPeriodic.CTIP1.equals(null));
		super.assertFalse("Test 3: CodesTableItemPeriodic.equals(Object) should not have matched", TestCodesTableItemPeriodic.CTIP1.equals("String"));
		super.assertFalse("Test 4: CodesTableItemPeriodic1.equals(CodesTableItemPeriodic2) should not have matched", TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP2));
		super.assertFalse("Test 4: CodesTableItemPeriodic1.equals(CodesTableItem2) should not have matched", TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItem.CTI2));
	}

	/**
	 * Test of hashCode().
	 */
	public void testHashCode() {
		assertEquals(
			"Equals is true, then hashCode is true",
			TestCodesTableItemPeriodic.CTIP1.equals(TestCodesTableItemPeriodic.CTIP5),
			TestCodesTableItemPeriodic.CTIP1.hashCode() == TestCodesTableItemPeriodic.CTIP5.hashCode());
	}
}