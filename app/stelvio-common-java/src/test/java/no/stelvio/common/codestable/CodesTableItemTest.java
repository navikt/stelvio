package no.stelvio.common.codestable;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit test of CodesTableItem.
 * 
 * @author personb66fa0b5ff6e
 * @version $Id$
 */
public class CodesTableItemTest {
	
	/**
	 * Test of constructor for CodesTableItem.
	 */
	@Test
	public void testCodesTableItemEmptyConstructor() {
		CodesTableItem cti = new Cti();
		
		assertEquals("Test 1: Code is not set", null, cti.getCode());
		assertEquals("Test 2: Decode is not set", null, cti.getDecode());
		assertEquals("Test 5: Locale is not set", null, cti.getLocale());
		assertEquals("Test 6: Validity is not set", null, cti.getIsValid());
	}

	/**
	 * Test of constructor for CodesTableItem()
	 */
	@Test
	public void testCodesTableItemConstrutor() {
		Locale locale = new Locale("nb", "NO");
		CodesTableItem cti = new Cti("t1code1", "t1decode1", locale, Boolean.TRUE);
		
		assertEquals("Test 1: Code is set", "t1code1", cti.getCode());
		assertEquals("Test 2: Decode is set", "t1decode1", cti.getDecode());
		assertEquals("Test 5: Locale is set", locale, cti.getLocale());
		assertEquals("Test 6: Validity is set", Boolean.TRUE, cti.getIsValid());
	}

	/**
	 * Test toString().
	 */
	@Test
	public void testToString() {
		CodesTableItem cti = new Cti("t1code1", "t1decode1", new Locale("nb", "NO"), Boolean.TRUE);
		assertNotNull("Test 1: toString is not null", cti.toString());
	}
	
	/**
	 * Test for boolean equals(Object).
	 */
	@Test
	public void testEqualsObject() {
		assertTrue("Test 1: CodesTableItem.equals(CodesTableItem) should have matched", TestCodesTableItem.getCti1().equals(TestCodesTableItem.getCti1()));
		assertFalse("Test 2: CodesTableItem.equals(null) should not have matched", TestCodesTableItem.getCti1().equals(null));
		assertFalse("Test 3: CodesTableItem.equals(Object) should not have matched", TestCodesTableItem.getCti1().equals("String"));
		assertFalse("Test 4: CodesTableItem1.equals(CodesTableItem2) should not have matched", TestCodesTableItem.getCti1().equals(TestCodesTableItem.getCti2()));
	}

	/**
	 * Test of hashCode().
	 */
	@Test
	public void testHashCode() {
		assertEquals(
			"Equals is true, then hashCode is true",
			TestCodesTableItem.getCti1().equals(TestCodesTableItem.getCti5()),
			TestCodesTableItem.getCti1().hashCode() == TestCodesTableItem.getCti5().hashCode());
	}

	/** Used for testing as {@link CodesTableItem} cannot be instantiated. */
	private static class Cti extends CodesTableItem {
		public Cti() {
		}		

		public Cti(String code, String decode, Locale locale, boolean isValid) {
			super(code, decode, locale, isValid);
		}
	}
}