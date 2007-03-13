package no.stelvio.domain.person;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class PidTest {

	String monthAbove9BostNr = "01327200336";
	String monthBelow10BostNr = "04250100286";
	String normalFnr = "12345678901";

	// Pids with legal whitespace
	String monthAbove9BostNrWs = "013272 00336";
	String monthBelow10BostNrWs = "042501 00286";
	String normalFnrWs = "260676 37924";

	// Pids with illeagal whitespace
	String monthAbove9BostNrIWs = "01327 200336";
	String monthBelow10BostNrIWs = "0425010 0286";
	String normalFnrIWs = "260 67637924";

	/** FIXME: Need valid D-number to implement this method */
	public void testDNummer() {

		// String dayBelow10Dnr = "41...";
		// String dayAbove9Dnr = "51...";

	}

	@Test
	public void whitespaceComplianceCheckIsOk() {
		// These three validations should be ok
		checkWhitespaceCompliance(monthAbove9BostNrWs, true);
		checkWhitespaceCompliance(monthBelow10BostNrWs, true);
		checkWhitespaceCompliance(normalFnrWs, true);

		// These three validations should NOT be ok
		checkWhitespaceCompliance(monthAbove9BostNrIWs, false);
		checkWhitespaceCompliance(monthBelow10BostNrIWs, false);
		checkWhitespaceCompliance(normalFnrIWs, false);
	}

	/** Test to make sure validation works with a valid fnr */
	@Test
	public void fnrCanBeValidated() {
		assertTrue("Could not create Pid using Fnr", Pid.isValidPid(normalFnr));
	}

	/**
	 * Testing the getFodselsdato method on Pid. If no exception is thrown, it's
	 * assumed that the method is working.
	 */
	@Test
	public void fodselsdatoCanBeRetrievedFromValidPid() {
		Pid pid = new Pid(monthAbove9BostNr);
		pid.getFodselsdato();
		pid = new Pid(monthBelow10BostNr);
		pid.getFodselsdato();
		pid = new Pid(normalFnr);
		pid.getFodselsdato();
	}

	/** Test to make sure validation works with valid bostnrs */
	@Test
	public void bostNummerCanBeValidated() {
		assertTrue("Could not create fnr using a Bostnummer with birth month > 9", Pid.isValidPid(monthAbove9BostNr));
		assertTrue("Could not create fnr using a Bostnummer with birth month > 10", Pid.isValidPid(monthAbove10BostNr));
	}

	private void checkWhitespaceCompliance(String pid, boolean isCompliant) {
		assertEquals("Whitespace test failed with Pid: " + pid, isCompliant, Pid.isWhitespaceCompliant(pid));		
	}	
}
