package no.nav.integration.framework.hibernate.formater;

import no.nav.integration.framework.hibernate.formater.LongFormater;
import junit.framework.TestCase;

/**
 * Test class for LongFormater class.
 * 
 * @author Thomas Kvalvag, Accenture
 */
public class LongFormaterTest extends TestCase {

	private LongFormater formater = new LongFormater();

	public void testFormatInput() {
		Object _Long = new Long(1710);
		String str = formater.formatInput(_Long);
		System.err.println("FormatInput:" +str);
		assertEquals("171{",str);
		
		_Long = new Long(-1710);
		str = formater.formatInput(_Long);
		System.err.println("FormatInput:" +str);
		assertEquals("171}",str);
		
		int start = 64; // @ eq 64 
		for(long i = 1; i <= 9; i++  ) {				
			_Long = new Long(i);
			str = formater.formatInput(_Long);
			System.err.println(str);
			int tmp = start + (int)i;
			System.out.println(((char)tmp) + "  " + tmp);
			assertEquals(((char)tmp) ,str.charAt(0));
		}
		
		start = 73; // I eq 73 
		for(long i = -1; -9 <= i; i--  ) {				
			_Long = new Long(i);
			str = formater.formatInput(_Long);
			System.err.println(str);
			
			int tmp = start + (int)Math.abs(i);
			System.out.println(((char)tmp) + "  " + tmp);
			assertEquals(((char)tmp) ,str.charAt(0));
		}
		
	}

	public void testFormatOutput() {
		Long res = (Long)formater.formatOutput("1234{");
		Long lonG = new Long(12340);
		System.err.println("FormatOutput" + res );
		assertEquals(res.toString(),lonG.toString());
		
		res = (Long)formater.formatOutput("1234}");
		lonG = new Long(-12340);
		System.err.println("FormatOutput" + res);
		assertEquals(res.toString(),lonG.toString());
	}

	public void testNullIsConvertedTo0() {
        Long res = (Long) formater.formatOutput(null);

		assertEquals("Problems when converting;", new Long(0), res);
	}
}
