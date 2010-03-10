package no.nav.datapower.util;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

public class PropertiesValidatorTest extends TestCase {

	private Properties hasMissing;
	private Properties hasUnknown;
	private Properties hasUnresolved;
	private Properties valid;
	private Properties required;
	
	PropertiesValidator missingValidator;
	PropertiesValidator unknownValidator;
	PropertiesValidator unresolvedValidator;
	PropertiesValidator validValidator;

	public PropertiesValidatorTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		required = new Properties();
		required.put("subset1.key1", "subset1key1value");
		required.put("subset1.key2", "subset1key2value");
		required.put("subset2.key1", "subset2key1value");
		required.put("subset2.key2", "subset2key2value");
	
		valid = new Properties();
		valid.put("subset1.key1", "somevalidvalue11");
		valid.put("subset1.key2", "somevalidvalue12");
		valid.put("subset2.key1", "somevalidvalue21");
		valid.put("subset2.key2", "somevalidvalue22");
		
		hasMissing = new Properties();
		hasMissing.put("subset1.key1", "somevalidvalue11");
		hasMissing.put("subset1.key2", "somevalidvalue12");
		hasMissing.put("subset2.key1", "somevalidvalue21");
		
		hasUnknown = new Properties();
		hasUnknown.put("subset1.key1", "somevalidvalue11");
		hasUnknown.put("subset1.key2", "somevalidvalue12");
		hasUnknown.put("subset2.key1", "somevalidvalue21");
		hasUnknown.put("subset2.key2", "somevalidvalue22");
		hasUnknown.put("subset2.key3", "somevalueofunknownkey");

		hasUnresolved = new Properties();
		hasUnresolved.put("subset1.key1", "somevalidvalue11");
		hasUnresolved.put("subset1.key2", "somevalidvalue12");
		hasUnresolved.put("subset2.key1", "${subset1.key1}");
		hasUnresolved.put("subset2.key2", "somevalidvalue22");
		
		missingValidator = getValidator(hasMissing);
		unknownValidator = getValidator(hasUnknown);
		unresolvedValidator = getValidator(hasUnresolved);
		validValidator = getValidator(valid);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private PropertiesValidator getValidator(Properties props) {
		return new PropertiesValidator(props, required);
	}
	public void testHasInvalidProperties() {
		assertTrue("Missing validator should have invalid properties", missingValidator.hasInvalidProperties());
		assertTrue("Unknown validator should have invalid properties", unknownValidator.hasInvalidProperties());
		assertTrue("Unresolved validator should have invalid properties", unresolvedValidator.hasInvalidProperties());
		assertTrue("Valid validator should not have invalid properties", !validValidator.hasInvalidProperties());		
	}

	public void testGetMissingProperties() {
		List<String> list = missingValidator.getMissingProperties();
		assertEquals("Missing validator should have missing property", 1, list.size());
		assertEquals("Missing validator should have missing property", "subset2.key2", list.get(0));
	}

	public void testHasMissingProperties() {
		assertTrue("Missing validator should have missing properties", missingValidator.hasInvalidProperties());
		assertTrue("Unknown validator should not have missing properties", !unknownValidator.hasMissingProperties());
		assertTrue("Unresolved validator should not have missing properties", !unresolvedValidator.hasMissingProperties());
		assertTrue("Valid validator should not have missing properties", !validValidator.hasMissingProperties());		
	}

	public void testGetUnknownProperties() {
		List<String> list = unknownValidator.getUnknownProperties();
		assertEquals("Unknown validator should have unkown property", 1, list.size());
		assertEquals("Unknown validator should have unkown property", "subset2.key3", list.get(0));
	}

	public void testHasUnknownProperties() {
		assertTrue("Missing validator should not have unknown properties", !missingValidator.hasUnknownProperties());
		assertTrue("Unknown validator should have unknown properties", unknownValidator.hasUnknownProperties());
		assertTrue("Unresolved validator should not have unkown properties", !unresolvedValidator.hasUnknownProperties());
		assertTrue("Valid validator should not have unknown properties", !validValidator.hasUnknownProperties());		
	}

	public void testGetUnresolvedProperties() {
		List<String> list = unresolvedValidator.getUnresolvedProperties();
		assertEquals("Unresolved validator should have unresolved property", 1, list.size());
		assertEquals("Unresolved validator should have unresolved property", "subset2.key1", list.get(0));
	}

	public void testHasUnresolvedProperties() {
		assertTrue("Missing validator should not have unresolved properties", !missingValidator.hasUnresolvedProperties());
		assertTrue("Unknown validator should not have unresolved properties", !unknownValidator.hasUnresolvedProperties());
		assertTrue("Unresolved validator should have unresolved properties", unresolvedValidator.hasUnresolvedProperties());
		assertTrue("Valid validator should not have unresolved properties", !validValidator.hasUnresolvedProperties());		
	}

	public void testGetUnresolvedPropertiesMap() {
		//fail("Not yet implemented");
	}

	public void testGetErrorMessage() {
		//fail("Not yet implemented");
	}
}
