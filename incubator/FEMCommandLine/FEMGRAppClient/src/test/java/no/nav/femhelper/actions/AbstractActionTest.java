package no.nav.femhelper.actions;

import java.util.Properties;

import junit.framework.TestCase;

public class AbstractActionTest extends TestCase {

	Properties dummyProperties = new Properties();
	
	public void testValidateEmptyCriteria() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "TESTSTRING";
		String criteria ="";
		
		assertTrue(aa.validate(eventValue, criteria));
	}

	public void testValidateCriteriaPartOfValue() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "TESTSTRING";
		String criteria ="TEST";
		
		assertTrue(aa.validate(eventValue, criteria));
	}

	public void testValidateEmptyValue() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "";
		String criteria ="TEST";
		
		assertFalse(aa.validate(eventValue, criteria));
	}

	public void testValidateCriteriaNotPartOfValue() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "TEMP";
		String criteria ="TEST";
		
		assertFalse(aa.validate(eventValue, criteria));
	}

	public void testValidateCriteriaSameAsValue() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "TEST";
		String criteria ="TEST";
		
		assertTrue(aa.validate(eventValue, criteria));
	}

	public void testValidateValueCriteriaEmpty() {
		AbstractAction aa = ActionFactory.getAction("DISCARD", dummyProperties);
		
		String eventValue = "";
		String criteria ="";
		
		assertTrue(aa.validate(eventValue, criteria));
	}
	
	

}
