package no.stelvio.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 * Test for configuration exceptions.
 * Tests configurations exceptions in common config.
 * @author person983601e0e117
 * @author person15754a4522e7
 */
public class TestConfigurationExceptions extends TestCase {
	
	/**
	 * Test missing property exception.
	 * <ul>
	 * <li>Tests that all properties entered into constructor, are stored in the exception.
	 * </ul> 
	 */
	public void testMissingPropertyException() {
		//Testdata
		String msg = "Required properties was not set by configuration";
		String[] propArray = new String[] { "prop1", "prop2", "prop3" };
		ArrayList<String> propList = new ArrayList<String>(3);
		propList.add("prop1");
		propList.add("prop2");

		//Test array constructor
		try {
			throw new MissingPropertyException(msg, propArray);
		} catch (MissingPropertyException e) {
			List excProps = Arrays.asList(e.getMissingProperties());

			assertEquals(msg, e.getMessage());
			assertEquals(3, e.getMissingProperties().length);
			assertTrue(excProps.contains("prop1"));
			assertTrue(excProps.contains("prop2"));
			assertTrue(excProps.contains("prop3"));
			assertNull(e.getCause());
		}
		
		//Test arraylist constructor
		try {
			throw new MissingPropertyException(msg, propList, new RuntimeException("Cause of missing property"));
		} catch (MissingPropertyException e) {
			List excProps = Arrays.asList(e.getMissingProperties());

			assertEquals(msg, e.getMessage());
			assertEquals(2, e.getMissingProperties().length);
			assertTrue(excProps.contains("prop1"));
			assertTrue(excProps.contains("prop2"));
			assertNotNull(e.getCause());
		}
		
	}
	
	/**
	 * Test conflicting property exception.
	 * <ul>
	 * <li>Tests that all properties entered into constructor, are stored in the exception.
	 * </ul> 
	 */
	public void testConflictingPropertyException() {
		try {
			throw new ConflictingPropertiesException("Properties have conflicting values", new Object[] {"prop1", "prop2", "prop3"});
		} catch (ConflictingPropertiesException e) {
			assertEquals("Properties have conflicting values", e.getMessage());
			assertEquals(3, e.getConflictingProperties().length);
		}
	}
	
	/**
	 * Test invalid property exception.
	 * <ul>
	 * <li>Tests that all properties entered into constructor, are stored in the exception.
	 * </ul> 
	 */
	public void testInvalidPropertyException() {
		try {
			throw new InvalidPropertyException("Properties have invalid values", new Object[] { "prop1", "prop2" });
		} catch (InvalidPropertyException e) {
			assertEquals(2, e.getInvalidProperties().length);
		}
	}
	
	/**
	 * Test property exceeds limit exception.
	 * <ul>
	 * <li>Tests that message, propertyname, min, max and cause entered into constructor, are stored in the exception.
	 * </ul> 
	 */
	public void testPropertyExceedsLimitException() {
		String msg = "Property is configured with a value that is not within the allowable limits";
		String prop = "propName";
		Integer min = new Integer(1);
		Double max = new Double(11);
		
		//Test msg, property name constructor
		try {
			throw new PropertyExceedsLimitException(msg, prop);
		} catch (PropertyExceedsLimitException e) {
			assertEquals(msg, e.getMessage());
			assertEquals(prop, e.getPropertyName());
			assertEquals(null, e.getCause());
		}

		//Test msg, property name, min, max constructor
		try {
			throw new PropertyExceedsLimitException(msg, prop, min, max);
		} catch (PropertyExceedsLimitException e) {
			assertEquals(msg, e.getMessage());
			assertEquals(prop, e.getPropertyName());
			assertEquals(min, e.getMinValue());
			assertEquals(max, e.getMaxValue());
			assertEquals(null, e.getCause());
		}

		//Test msg, property name, min, max, cause constructor
		try {
			throw new PropertyExceedsLimitException(msg, prop, min, max, new NullPointerException("Tester cause"));
		} catch (PropertyExceedsLimitException e) {
			assertEquals(msg, e.getMessage());
			assertEquals(prop, e.getPropertyName());
			assertEquals(min, e.getMinValue());
			assertEquals(max, e.getMaxValue());
			assertNotNull(e.getCause());
		}
	}
}
