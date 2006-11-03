package no.stelvio.common.codestable;

import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

/**
 * Unit test of CodesTableItem.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableItemTest extends TestCase {

	/**
	 * Constructor for CodesTableItemTest.
	 * @param arg0
	 */
	public CodesTableItemTest(String arg0) {
		super(arg0);
	}
	
	/**
	 * Test of constructor for CodesTableItem.
	 */
	public void testCodesTableItemEmptyConstructor() {
		CodesTableItem cti = new CodesTableItem();
		
		assertEquals("Test 1: Code is not set", null, cti.getCode());
		assertEquals("Test 2: Decode is not set", null, cti.getDecode());
		//assertEquals("Test 3: Fromdate is not set", null, cti.getFromDate());
		//assertEquals("Test 4: Todate is not set", null, cti.getToDate());
		assertEquals("Test 5: Locale is not set", null, cti.getLocale());
		assertEquals("Test 6: Validity is not set", null, cti.getIsValid());
	}

	/**
	 * Test of constructor for CodesTableItem()
	 */
	public void testCodesTableItemConstrutor() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		//date, date,
		/*
		CodesTableItem cti = new CodesTableItem("t1code1", "t1decode1", locale, Boolean.TRUE);
		
		assertEquals("Test 1: Code is set", "t1code1", cti.getCode());
		assertEquals("Test 2: Decode is set", "t1decode1", cti.getDecode());
		//assertEquals("Test 3: Fromdate is set", date, cti.getFromDate());
		//assertEquals("Test 4: Todate is set", date, cti.getToDate());
		assertEquals("Test 5: Locale is set", locale, cti.getLocale());
		assertEquals("Test 6: Validity is set", Boolean.TRUE, cti.getIsValid());
		*/
	}

	/**
	 * Test of CodesTableItem's getters and setters.
	 */
	public void testSettersAndGetters() {
		
		Date date = new Date();
		Locale locale = new Locale("nb", "NO");
		
		CodesTableItem cti = new CodesTableItem();
		cti.setCode("t1code1");
		cti.setDecode("t1decode1");
		//cti.setFromDate(date);
		//cti.setToDate(date);
		cti.setLocale(locale);
		cti.setIsValid(Boolean.TRUE);
		
		assertEquals("Test 1: Code is set", "t1code1", cti.getCode());
		assertEquals("Test 2: Decode is set", "t1decode1", cti.getDecode());
		//assertEquals("Test 3: FromDate is set", date, cti.getFromDate());
		//assertEquals("Test 4: Todate is set", date, cti.getToDate());
		assertEquals("Test 5: Locale is set", locale, cti.getLocale());
		assertEquals("Test 6: Validity is set", Boolean.TRUE, cti.getIsValid());
	}

	/**
	 * Test toString().
	 */
	public void testToString() {
		//new Date(), new Date(),
		//CodesTableItem cti = new CodesTableItem("t1code1", "t1decode1", new Locale("nb", "NO"), Boolean.TRUE);
		//assertNotNull("Test 1: toString is not null", cti.toString());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	public void testEqualsObject() {
	
		super.assertTrue("Test 1: CodesTableItem.equals(CodesTableItem) should have matched", TestCodesTableItem.CTI1.equals(TestCodesTableItem.CTI1));
		super.assertFalse("Test 2: CodesTableItem.equals(null) should not have matched", TestCodesTableItem.CTI1.equals(null));
		super.assertFalse("Test 3: CodesTableItem.equals(Object) should not have matched", TestCodesTableItem.CTI1.equals("String"));
		super.assertFalse("Test 4: CodesTableItem1.equals(CodesTableItem2) should not have matched", TestCodesTableItem.CTI1.equals(TestCodesTableItem.CTI2));
	}

	/**
	 * Test of hashCode().
	 *
	 */
	public void testHashCode() {
		assertEquals(
			"Equals is true, then hashCode is true",
			TestCodesTableItem.CTI1.equals(TestCodesTableItem.CTI5),
			TestCodesTableItem.CTI1.hashCode() == TestCodesTableItem.CTI5.hashCode());
	}
}