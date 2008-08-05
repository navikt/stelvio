package no.nav.datapower.util;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

public class DPPropertiesUtilsTest extends TestCase {

	private Properties props;

	public DPPropertiesUtilsTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		props = new Properties();
		props.put("subset1.key1", "subset1key1value");
		props.put("subset1.key2", "subset1key2value");
		props.put("subset2.key1", "subset2key1value");
		props.put("subset2.key2", "subset2key2value");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testStripWhiteSpaces() {
//		fail("Not yet implemented");
	}

	public void testSubset() {
		Properties subset1 = DPPropertiesUtils.subset(props, "subset1");
		Properties subset2 = DPPropertiesUtils.subset(props, "subset2");
		assertEquals("Subset1 should only have two properties", 2, subset1.size());
		assertEquals("Subset2 should only have two properties", 2, subset2.size());
	}

	public void testGetSubsetsProperties() {
		List<Properties> propsList = DPPropertiesUtils.getSubsets(props);
		assertEquals("Should find only two subsets", 2, propsList.size());
	}
	
	public void testListSubsetKeys() {
//		fail("Not yet implemented");
		
		List<String> subsets = DPPropertiesUtils.listSubsetKeys(props);
		assertTrue("Should contain 'subset1'", subsets.contains("subset1"));
		assertTrue("Should contain 'subset2'", subsets.contains("subset2"));
	}

	public void testGetNestedSubsetsPropertiesStringArray() {
//		fail("Not yet implemented");
	}

	public void testGetNestedSubsetsExtendedPropertiesStringArray() {
//		fail("Not yet implemented");
	}

}
