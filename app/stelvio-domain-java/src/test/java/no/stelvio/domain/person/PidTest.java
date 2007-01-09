package no.stelvio.domain.person;

import java.util.Date;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.person.PidValidationException;
import junit.framework.TestCase;

public class PidTest extends TestCase {

	String monthAbove9BostNr = "01327200336";
	String monthBelow10BostNr = "04250100286";
	String normalFnr = "12345678901";
	
	/**
	 * FIXME: Need valid D-number to implement this method
	 *
	 */
	public void testDNummer(){
		
		String dayBelow10Dnr = "41...";
		String dayAbove9Dnr = "51...";
		
	}
	
	/**
	 * Test to make sure validation works with a valid fnr
	 *
	 */
	public void testFnr(){
			boolean valid = Pid.isValidPidNum(normalFnr);
		if(!valid){
			fail("Could not create Pid using Fnr");
		}

	}
	
	public void testGetDate(){
		Pid pid = new Pid(monthAbove9BostNr);
		Date date = pid.getDate();
		pid = new Pid(monthBelow10BostNr);
		date = pid.getDate();			
		pid = new Pid(normalFnr);
		date = pid.getDate();
	}
	
	/**
	 * Test to make sure validation works with valid bostnrs
	 *
	 */
	public void testBostNummer(){
		boolean valid = Pid.isValidPidNum(monthAbove9BostNr);
		if(!valid)
			fail("Could not create fnr using a Bostnummer with birth month > 9");

		valid = Pid.isValidPidNum(monthBelow10BostNr);
		if(!valid)
			fail("Could not create Pid using a Bostnummer with birth month < 10");
	}
}
