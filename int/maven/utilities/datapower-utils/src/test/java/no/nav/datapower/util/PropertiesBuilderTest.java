package no.nav.datapower.util;

import java.util.Properties;

import junit.framework.TestCase;

public class PropertiesBuilderTest extends TestCase {

	private Properties props1;
	private Properties props2;
	private Properties props3;
	
	public PropertiesBuilderTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
		props1 = new Properties();
		props1.put("prop1", "valueA");
		props1.put("prop2", "valueB");
		props1.put("prop3", "valueC");
		props1.put("prop4", "valueD");
	
		props2 = new Properties();
		props2.put("prop4", "valueE");

		props3 = new Properties();
		props3.put("prop1", "valueA");
		props3.put("prop2", "valueB");
		props3.put("prop3", "${prop1}");
		props3.put("prop4", "${prop2}_${prop3}");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBuildProperties() {
		Properties props = new PropertiesBuilder().putAll(props1).buildProperties();
		assertNotNull(props);
		assertEquals(4, props.size());
	}
	
	public void testBuildOverriddenProperties() {
		Properties props = new PropertiesBuilder().putAll(props1).putAll(props2).buildProperties();
		assertNotNull(props);
		assertEquals(4, props.size());
		assertEquals("valueE", props.get("prop4"));
	}

	public void testBuildInterpolatedProperties() {
		Properties props = new PropertiesBuilder().putAll(props3).interpolate().buildProperties();
		assertNotNull(props);
		assertEquals(4, props.size());
		assertEquals("valueA", props.get("prop3"));
		assertEquals("valueB_valueA", props.get("prop4"));
	}

	public void testBuildExtendedProperties() {
//		fail("Not yet implemented");
	}

}
