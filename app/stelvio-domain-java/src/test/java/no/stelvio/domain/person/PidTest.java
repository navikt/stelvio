package no.stelvio.domain.person;

import junit.framework.TestCase;

public class PidTest extends TestCase {

	String monthAbove9BostNr = "01327200336";
	String monthBelow10BostNr = "04250100286";
	String normalFnr = "12345678901";
	
	//Pids with legal whitespace
	String monthAbove9BostNrWs = "013272 00336";
	String monthBelow10BostNrWs = "042501 00286";
	String normalFnrWs = "260676 37924";	
	
	//Pids with illeagal whitespace
	String monthAbove9BostNrIWs = "01327 200336";
	String monthBelow10BostNrIWs = "0425010 0286";
	String normalFnrIWs = "260 67637924";		
	
	/**
	 * FIXME: Need valid D-number to implement this method
	 *
	 */
	public void testDNummer(){
		
	//	String dayBelow10Dnr = "41...";
	//	String dayAbove9Dnr = "51...";
		
	}
	
	
	public void testWhitespaceFormats(){
		boolean valid = true;
		
		//These three validations should be ok
		valid = Pid.isWhitespaceCompliant(monthAbove9BostNrWs);
		if(!valid){
			fail("Whitespace test failed with format: "+monthAbove9BostNrWs);
		}
		valid = Pid.isWhitespaceCompliant(monthBelow10BostNrWs);
		if(!valid){
			fail("Whitespace test failed with format: "+monthBelow10BostNrWs);
		}
		valid = Pid.isWhitespaceCompliant(normalFnrWs);
		if(!valid){
			fail("Whitespace test failed with format: "+normalFnrWs);
		}	
		
		//These three validations should NOT be ok
		valid = Pid.isWhitespaceCompliant(monthAbove9BostNrIWs);
		if(valid){
			fail("Whitespace test failed with format: "+monthAbove9BostNrIWs);
		}	
		valid = Pid.isWhitespaceCompliant(monthBelow10BostNrIWs);
		if(valid){
			fail("Whitespace test failed with format: "+monthBelow10BostNrIWs);
		}
		valid = Pid.isWhitespaceCompliant(normalFnrIWs);
		if(valid){
			fail("Whitespace test failed with format: "+normalFnrIWs);
		}		
		
	}
	
	/**
	 * Test to make sure validation works with a valid fnr
	 *
	 */
	public void testFnr(){
			boolean valid = Pid.isValidPid(normalFnr);
		if(!valid){
			fail("Could not create Pid using Fnr");
		}

	}
	
	/**
	 * Testing the getFodselsdato method on Pid.
	 * If no exception is thrown, it's assumed that the method is working.
	 *
	 */
	public void testGetFodselsdato(){
		try{
			Pid pid = new Pid(monthAbove9BostNr);
			pid.getFodselsdato();
			pid = new Pid(monthBelow10BostNr);			
			pid.getFodselsdato();			
			pid = new Pid(normalFnr);
			pid.getFodselsdato();
		}catch(PidValidationException e){
			e.printStackTrace();
			fail("The getFodselsdato method failed during execution");
		}
	}
	
	/**
	 * Test to make sure validation works with valid bostnrs
	 *
	 */
	public void testBostNummer(){
		boolean valid = Pid.isValidPid(monthAbove9BostNr);
		if(!valid)
			fail("Could not create fnr using a Bostnummer with birth month > 9");

		valid = Pid.isValidPid(monthBelow10BostNr);
		if(!valid)
			fail("Could not create Pid using a Bostnummer with birth month < 10");
	}
}
