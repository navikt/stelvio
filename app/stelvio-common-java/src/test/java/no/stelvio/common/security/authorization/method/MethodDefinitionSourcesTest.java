package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.method.MethodDefinitionSource;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MethodDefinitionSources.
 * 
 * @author ??
 * 
 */
public class MethodDefinitionSourcesTest {

	private MethodDefinitionSources sources;

	/**
	 * Set up sources before testing.
	 * 
	 * @throws Exception
	 *             exception
	 */
	@Before
	public void setUp() throws Exception {
		sources = new MethodDefinitionSources();
		// sources.setMethodDefinitionAttributes(methodDefinitionAttributes);
		// sources.setMethodDefinitionMap(methodDefinitionMap);
	}

	/**
	 * Create method definition source.
	 * 
	 * @param attr
	 *            attributes
	 * @return source
	 */
	private MethodDefinitionSource createMethodDefinitionSource(String[] attr) {
		MockMethodDefinitionSource methodDefinitionSource = new MockMethodDefinitionSource();
		ConfigAttributeDefinition def1 = new ConfigAttributeDefinition();
		for (String string : attr) {
			def1.addConfigAttribute(new SecurityConfig(string));
		}
		methodDefinitionSource.setDefinition(def1);
		return methodDefinitionSource;
	}

	/**
	 * Testing afterPropertiesSet.
	 */
	@Test
	public void testAfterPropertiesSet() {
		try {
			sources.afterPropertiesSet();
			fail("Should have thrown IllegalArgumentException");
		} catch (Exception expected) {
			if (expected instanceof IllegalArgumentException) {
				assertTrue(true);
			} else {
				fail("Unexpected exception caught." + expected.getCause());
			}
		}
	}

	/**
	 * Test getAttributes from just one method definition source.
	 */
	@Test
	public void testGetAttributesFromJustOneMethodDefinitionSource() {
		String[] attr = { "methodDefinitionAttribute1", "methodDefinitionAttribute2", "methodDefinitionAttribute3" };
		MethodDefinitionSource methodDefinitionAttributes = createMethodDefinitionSource(attr);
		sources.setMethodDefinitionAttributes(methodDefinitionAttributes);
		ConfigAttributeDefinition def = sources.getAttributes(new Object());
		assertNotNull(def);
		assertEquals(methodDefinitionAttributes.getAttributes(null).size(), def.size());
		assertEquals(methodDefinitionAttributes.getAttributes(null), def);
	}

	/**
	 * Test getAttributes from both method definition sources.
	 */
	@Test
	public void testGetAttributesFromBothMethodDefinitionSources() {

		String[] attr = { "attribute1", "attribute2", "attribute3" };
		MethodDefinitionSource methodDefinitionAttributes = createMethodDefinitionSource(attr);
		sources.setMethodDefinitionAttributes(methodDefinitionAttributes);
		String[] attr2 = { "attribute4", "attribute3", "attribute5" };
		MethodDefinitionSource methodDefinitionMap = createMethodDefinitionSource(attr2);
		sources.setMethodDefinitionMap(methodDefinitionMap);

		ConfigAttributeDefinition def = sources.getAttributes(new Object());
		assertNotNull(def);
		assertEquals("The two sources should have merged and duplicate attributes removed.", 5, def.size());

	}

	/**
	 * Test get and set method definition attributes.
	 */
	@Test
	public void testGetAndSetMethodDefinitionAttributes() {
		String[] attr = { "attribute1", "attribute2", "attribute3" };
		MethodDefinitionSource methodDefinitionAttributes = createMethodDefinitionSource(attr);
		sources.setMethodDefinitionAttributes(methodDefinitionAttributes);
		assertEquals(methodDefinitionAttributes, sources.getMethodDefinitionAttributes());
	}

	/**
	 * Test get and set method definition map.
	 */
	@Test
	public void testGetAndSetMethodDefinitionMap() {
		String[] attr = { "attribute1", "attribute2", "attribute3" };
		MethodDefinitionSource methodDefinitionMap = createMethodDefinitionSource(attr);
		sources.setMethodDefinitionMap(methodDefinitionMap);
		assertEquals(methodDefinitionMap, sources.getMethodDefinitionMap());
	}

}
