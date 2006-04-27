package no.nav.common.framework.util;

import no.nav.common.framework.service.ServiceFailedException;
import no.nav.common.framework.util.StringHelper;

import junit.framework.TestCase;

/**
 * This class is a JUnit testclass for StringHelper
 * 
 * @author person5b7fd84b3197, Accenture
 */
public class StringHelperTest extends TestCase {

	public StringHelperTest(String name) {
		super(name);
	}

	/**
	 * Test getProperty.
	 *
	 */
	public void testGetProperty() {
		String record = "AAAAABBBBBBCCCCCCCDDDDDDD";
		int offset = 11;
		int length = 7;
		String result = StringHelper.getProperty( length,offset, record );
		assertEquals(result,"CCCCCCC");
	}

	/**
	 * Test AppendProperty
	 *
	 */
	public void testAppendProperty() throws ServiceFailedException {
		StringBuffer buff = new StringBuffer();
		Object value = "12345";
		String type = "string";
		int length = 10;
		StringHelper.appendProperty(length, value, buff,type );
		assertEquals( length, buff.length() );
		
		Object obj = new Integer(22);
		type = "integer";
		int length1 = 8;
		StringHelper.appendProperty(length1, obj, buff,type );
		System.out.println("Buff:" + buff.toString() + ":");
		assertEquals( length + length1 , buff.length() );
		
		StringBuffer buff1 = new StringBuffer();
		Object obj1 = new Integer(1);
		type = "int";
		StringHelper.appendProperty(2, obj1, buff1,type );
		assertEquals( 2 , buff1.length() );
		
	}
	
	/**
	 * Test AppendProperty
	 *
	 */
	public void testAppendPropertyWithToLongInput() {
		StringBuffer buff = new StringBuffer();
		Object value = "12345";
		String type = "string";
		int length = 4;
		try {
			StringHelper.appendProperty(length, value, buff,type );
			fail("Did not throw exception when input is to long");
		} catch (ServiceFailedException e) {
			// Should be thrown
		}	
		
	}
	
	/**
	 * Test AppendProperty
	 *
	 */
	public void testAppendPropertyWithLengtEQInputlength() throws ServiceFailedException {
		StringBuffer buff = new StringBuffer();
		Object value = "12345";
		String type = "string";
		int length = 5;
		StringHelper.appendProperty(length, value, buff,type );
		assertEquals(length, buff.length());
	}
	
	
	/**
	 * Tests removeEndingSpaces
	 *
	 */
	public void testRemoveEndingSpaces() {
		String testString1 = "         ";
		String result1 = StringHelper.removeEndingSpaces(testString1);
		assertEquals("",result1);
		
		String testString2 = "AAAA A ";
		String result2 = StringHelper.removeEndingSpaces(testString2);
		assertEquals("AAAA A",result2);
		
		String testString3 = "AAAA A ";
		String result3 = StringHelper.removeEndingSpaces(testString3);
		assertEquals("AAAA A",result3);
		
		String testString4 = "A ";
		String result4 = StringHelper.removeEndingSpaces(testString4);
		assertEquals("A",result4);
	}

	/**
	 * Tests insertLeadingZeros
	 *
	 */
	public void testInsertLeadingZeros(){
		
		assertTrue(StringHelper.insertLeadingZeros("123", 5).equals("00123"));
		assertTrue(StringHelper.insertLeadingZeros("123", 3).equals("123"));
		assertFalse(StringHelper.insertLeadingZeros("123", 4).equals("123"));
	}
}
