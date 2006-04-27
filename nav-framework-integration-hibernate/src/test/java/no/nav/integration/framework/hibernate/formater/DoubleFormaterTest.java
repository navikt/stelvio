package no.nav.integration.framework.hibernate.formater;

import no.nav.integration.framework.hibernate.formater.DoubleFormater;
import junit.framework.TestCase;

/**
 * JUnit Test of DoubleFormater
 *
 * @author person5b7fd84b3197, Accenture
 */
public class DoubleFormaterTest extends TestCase {
	DoubleFormater formater = new DoubleFormater();


	public void testFormatInput() {
		Double value = new Double(10.1234);
		formater.setLength(4);
		formater.setPrecision(3);
		String result = formater.formatInput(value);
		assertEquals("Test 1: test Input", result, "0123");

		formater.setLength(3);
		formater.setPrecision(1);
		result = formater.formatInput(value);
		assertEquals("Test 2: test Input", result, "101");


		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(-0.1234);
		result = formater.formatInput(value);
		assertEquals("Test 3: ", result, "012L");


		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(0.1234);
		result = formater.formatInput(value);
		System.err.println("TEST: " + result);
		assertEquals("Test 4: ", result, "012C");

		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(0.000);
		result = formater.formatInput(value);
		System.err.println("TEST: " + result);
		assertEquals("Test 5,5: ", result, "000{");

		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(-1.0);
		result = formater.formatInput(value);
		System.err.println("TEST 6: " + result);
		assertEquals("Test 6: ", result, "100}");

		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(-0.100);
		System.err.println("-----------------------------");
		result = formater.formatInput(value);
		System.err.println("TEST5: " + result);
		assertEquals("Test 5: ", result, "010}");

		formater.setLength(4);
		formater.setPrecision(3);
		formater.setSigned(true);
		value = new Double(0.22);

		result = formater.formatInput(value);
		System.err.println("TEST6: " + result);
		assertEquals("Test 6: ", result, "022{");
	}

	public void testFormatOutput() {
		String value = "1234";
		formater.setLength(4);
		formater.setPrecision(3);

		Double result = (Double) formater.formatOutput(value);

		assertEquals("Test 1: Test format output", result, new Double(1.234));


	}

	public void testSetPrecision() {
		formater.setPrecision(10);
		assertEquals("Test 1: set and get length", 10, formater.getPrecision());

	}


	public void testSetLength() {
		formater.setLength(10);
		assertEquals("Test 1: set and get length", 10, formater.getLength());
	}

}
