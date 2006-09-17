/*
 * Created on 08.sep.04
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package no.stelvio.integration.framework.hibernate.formater;

import no.stelvio.integration.framework.hibernate.formater.CicsStringFormater;
import junit.framework.TestCase;

/**
 * @author TKC2920
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CicsStringFormaterTest extends TestCase {

	CicsStringFormater formater = new CicsStringFormater();

	public void testFormatInput() {
		formater.setToUpper(false);
		String result = formater.formatInput("aaaÅåÅbbbbæÆæccccøøØØdddd");
		assertEquals("aaa}}}bbbb{{{cccc¦¦¦¦dddd",result);
	}

	public void testFormatOutput() {
		formater.setToUpper(false);
		String result = (String)formater.formatOutput("aaa}}}bbbb{{{cccc¦¦¦¦dddd");
		assertEquals("aaaÅÅÅbbbbÆÆÆccccØØØØdddd",result);
	}
	
	public void testFormatInput2() {
		formater.setToUpper(true);
		String result = formater.formatInput("aaaÅåÅbbbbæÆæccccøøØØdddd");
		assertEquals("AAA}}}BBBB{{{CCCC¦¦¦¦DDDD",result);
	}

	public void testFormatOutput2() {
		formater.setToUpper(true);
		String result = (String)formater.formatOutput("aaa}}}bbbb{{{cccc¦¦¦¦dddd");
		assertEquals("aaaÅÅÅbbbbÆÆÆccccØØØØdddd".toUpperCase(),result);
	}

}
