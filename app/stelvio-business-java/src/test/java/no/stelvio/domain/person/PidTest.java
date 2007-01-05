package no.stelvio.domain.person;

import no.stelvio.domain.person.Pid;
import no.stelvio.domain.person.PidValidationException;
import junit.framework.TestCase;

public class PidTest extends TestCase {

	/**
	 * FIXME: Need valid D-number to implement this method
	 *
	 */
	public void testDNummer(){
		
		String dayBelow10Dnr = "41...";
		String dayAbove9Dnr = "51...";
		
	}
	
	/**
	 * Test to make sure validation in Pid constructor works with a valid fnr
	 *
	 */
	public void testFnr(){
		try{
			Pid pid = new Pid("12345678901");
		}catch(PidValidationException e){
			fail("Could not create Pid using Fnr");
		}

	}
	
	/**
	 * Test to make sure validation in Pid constructor works with valid bostnrs
	 *
	 */
	public void testBostNummer(){
		String monthAbove9BostNr = "01327200336";
		String monthBelow10BostNr = "04250100286";
		try{
			Pid pid = new Pid(monthAbove9BostNr);
		}catch(PidValidationException e){
			fail("Could not create fnr using a Bostnummer with birth month > 9");
		}
		
		try{	
			Pid pid = new Pid(monthBelow10BostNr);
		}catch(PidValidationException e){
			fail("Could not create Pid using a Bostnummer with birth month < 10");
		}		
		
	}	
	
}
