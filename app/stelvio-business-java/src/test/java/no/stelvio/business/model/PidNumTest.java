package no.stelvio.business.model;

import no.stelvio.business.model.pidnum.PidNum;
import no.stelvio.business.model.pidnum.PidNumException;
import junit.framework.TestCase;

public class PidNumTest extends TestCase {

	/**
	 * FIXME: Need valid D-number to implement this method
	 *
	 */
	public void testDNummer(){
		
		String dayBelow10Dnr = "41...";
		String dayAbove9Dnr = "51...";
		
	}
	
	/**
	 * Test to make sure validation in PidNum constructor works with a valid fnr
	 *
	 */
	public void testFnr(){
		try{
			PidNum pid = new PidNum("12345678901");
		}catch(PidNumException e){
			fail("Could not create Pid using Fnr");
		}

	}
	
	/**
	 * Test to make sure validation in PidNum constructor works with valid bostnrs
	 *
	 */
	public void testBostNummer(){
		String monthAbove9BostNr = "01327200336";
		String monthBelow10BostNr = "04250100286";
		try{
			PidNum pid = new PidNum(monthAbove9BostNr);
		}catch(PidNumException e){
			fail("Could not create fnr using a Bostnummer with birth month > 9");
		}
		
		try{	
			PidNum pid = new PidNum(monthBelow10BostNr);
		}catch(PidNumException e){
			fail("Could not create Pid using a Bostnummer with birth month < 10");
		}		
		
	}	
	
}
