package no.stelvio.common.error.old;

import junit.framework.TestCase;

/**
 * ErrorConfig unit test..
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 1954 $ $Author: psa2920 $ $Date: 2005-02-08 14:35:42 +0100 (Tue, 08 Feb 2005) $
 */
public class ErrorConfigTest extends TestCase {

	/**
	 * Constructor for ErrorConfigTest.
	 * @param arg0
	 */
	public ErrorConfigTest(String arg0) {
		super(arg0);
	}

	public void testErrorConfig() {
		ErrorConfig ec = new ErrorConfig();
		assertEquals("Code is not set", -1, ec.getCode());
		assertEquals("Severity is not set", null, ec.getSeverity());
		assertNull("Message is not set", ec.getMessage());
	}

	public void testErrorConfigCodeSeverityMessage() {
		ErrorConfig ec = new ErrorConfig(42, new Integer(66), "Hiiiyyyyaaargh!");
		assertEquals("Code is set", 42, ec.getCode());
		assertEquals("Severity is set", new Integer(66), ec.getSeverity());
		assertEquals("Message is set", "Hiiiyyyyaaargh!", ec.getMessage());
	}

	public void testSettersAndGetters() {
		ErrorConfig ec = new ErrorConfig();
		ec.setCode(42);
		ec.setSeverity(new Integer(66));
		ec.setMessage("Hiiiyyyyaaargh!");

		assertEquals("Code is set", 42, ec.getCode());
		assertEquals("Severity is set", new Integer(66), ec.getSeverity());
		assertEquals("Message is set", "Hiiiyyyyaaargh!", ec.getMessage());
	}

	public void testToString() {
		ErrorConfig ec = new ErrorConfig(42, new Integer(66), "Hiiiyyyyaaargh!");
		assertNotNull("toString is not null", ec.toString());
	}
}
