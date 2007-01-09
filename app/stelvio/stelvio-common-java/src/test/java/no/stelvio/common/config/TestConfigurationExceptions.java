package no.stelvio.common.config;

import junit.framework.TestCase;

public class TestConfigurationExceptions extends TestCase {

	public void testMissingPropertyException(){
		try{
			throw new MissingPropertyException("prop1", "prop2", "prop3");
		}catch(MissingPropertyException e){
			assertEquals(e.getTemplateArguments().length,3);
		}catch(Exception e){
			fail("Some other exception than MissingPropertyException was thrown, " +
					"probably because MissingPropertyException isn't made in accordance to the Stelvio Common Error guidelines");
		}
	}
	
	
	public void testConflictingPropertyException(){
		try{
			throw new ConflictingPropertiesException("prop1", "prop2", "prop3");
		}catch(ConflictingPropertiesException e){
			assertEquals(e.getTemplateArguments().length,3);
		}catch(Exception e){
			fail("Some other exception than ConflictingPropertiesException was thrown, " +
					"probably because ConflictingPropertiesException isn't made in accordance to the Stelvio Common Error guidelines");
		}		
	}
	
	public void testInvalidPropertyException(){
		try{
			throw new InvalidPropertyException("prop1", "prop2");
		}catch(InvalidPropertyException e){
			assertEquals(e.getTemplateArguments().length,2);
		}catch(Exception e){
			fail("Some other exception than InvalidPropertyException was thrown, " +
					"probably because InvalidPropertyException isn't made in accordance to the Stelvio Common Error guidelines");
		}		
	}	
	
	public void testPropertyExceedsLimitException(){
		try{
			throw new PropertyExceedsLimitException("testProp");
		}catch(PropertyExceedsLimitException e){	
			assertEquals(e.getCause(), null);
		}catch(Exception e){
			fail("Some other exception than PropertyExceedsLimitException was thrown, " +
					"probably because PropertyExceedsLimitException isn't made in accordance to the Stelvio Common Error guidelines");
		}
		
		try{
			throw new PropertyExceedsLimitException("testProp", new Integer(1), new Double(11));
		}catch(PropertyExceedsLimitException e){		
			assertEquals(e.getCause(), null);
		}catch(Exception e){
			fail("Some other exception than PropertyExceedsLimitException was thrown, " +
					"probably because PropertyExceedsLimitException isn't made in accordance to the Stelvio Common Error guidelines");
		}
		
		try{
			throw new PropertyExceedsLimitException(new NullPointerException("Tester cause"),"testProp", new Integer(1), new Double(11));
		}catch(PropertyExceedsLimitException e){
			assertNotNull(e.getCause());
		}catch(Exception e){
			fail("Some other exception than PropertyExceedsLimitException was thrown, " +
					"probably because PropertyExceedsLimitException isn't made in accordance to the Stelvio Common Error guidelines");
		}
	}	
	
	
}
