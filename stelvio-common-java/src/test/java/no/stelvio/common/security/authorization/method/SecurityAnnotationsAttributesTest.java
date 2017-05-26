package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

/**
 * Test class for SecurityAnnotationsAttributes.
 * 
 * @author ??
 */
public class SecurityAnnotationsAttributesTest {

	private SecurityAnnotationsAttributes attributes;

	/**
	 * Set up attributes and mapping.
	 */
	@Before
	public void setUp() {
		// create the Annotations support
		this.attributes = new SecurityAnnotationsAttributes();
		setUpMapping();
	}

	/**
	 * Set up mapping.
	 */
	private void setUpMapping() {
		AnnotationAttributesMapping map = new AnnotationAttributesMapping();
		List<ConfigAttribute> id1 = new ArrayList<ConfigAttribute>();
		id1.add(new SecurityConfig("provider1"));
		id1.add(new SecurityConfig("provider2"));
		List<ConfigAttribute> id2 = new ArrayList<ConfigAttribute>();
		id2.add(new SecurityConfig("provider3"));
		id2.add(new SecurityConfig("provider4"));
		List<ConfigAttribute> id3 = new ArrayList<ConfigAttribute>();
		id3.add(new SecurityConfig("provider5"));
		id3.add(new SecurityConfig("provider6"));
		map.addProviders("Id1", id1);
		map.addProviders("Id2", id2);
		map.addProviders("Id3", id3);
		attributes.setAnnotationMapping(map);
	}

	/**
	 * Test getAtytributes for class.
	 */
	@Test
	public void getAttributesClass() {
		Collection<ConfigAttribute> attrs = this.attributes.getAttributes(MockService.class);
		assertNotNull(attrs);
		// expect 1 annotation mapped to 2 providers
		assertTrue(attrs.size() == 2);
		Collection<ConfigAttribute> providers = this.attributes.getAnnotationMapping().getProviders("Id1");
		assertNotNull(providers);
		assertEquals(providers, attrs);
	}

	/**
	 * Test getAttributes for class with annotation attributes not present in mapping.
	 */
	@Test
	public void getAttributesClassWithAnnotationAttributeNotPresentInMapping() {
		// Class is annotated with @Secured({"IdThatDoesNotExistInMapping"}) which is not present in the mapping
		try {
			this.attributes.getAttributes(MockService2.class);
			fail("Should have thrown SecureAnnotationNotInMappingException.");
		} catch (SecureAnnotationNotInMappingException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Test getAttributes for class and no filter class.
	 */
	@Test
	public void getAttributesClassClass() {

		try {
			this.attributes.getAttributes(MockService.class, null);
			fail("Unsupported method should have thrown an exception!");
		} catch (UnsupportedOperationException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Test getAttributes for method.
	 */
	@Test
	public void getAttributesMethod() {

		Method method = null;

		try {
			method = MockService.class.getMethod("someProtectedMethod");
		} catch (NoSuchMethodException unexpected) {
			fail("Should be a method called 'someProtectedMethod' on class!");
		}

		Collection<ConfigAttribute> attrs = this.attributes.getAttributes(method);

		assertNotNull(attrs);
		// expect 2 attributes
		assertTrue(attrs.size() == 2);

		Collection<ConfigAttribute> providers = this.attributes.getAnnotationMapping().getProviders("Id2");
		assertNotNull(providers);

		for (ConfigAttribute obj : attrs) {
			assertTrue(providers.contains(obj));
		}
	}

	/**
	 * Test getAttributes for method with annotation attribute not present in mapping.
	 */
	@Test
	public void getAttributesMethodWithAnnotationAttributeNotPresentInMapping() {
		Method method = null;

		try {
			method = MockService2.class.getMethod("someProtectedMethod");
		} catch (NoSuchMethodException unexpected) {
			fail("Should be a method called 'someProtectedMethod' on class!");
		}
		// Method is annotated with @Secured({"Id2","AnotherIdThatDoesNotExistInMapping"})
		// where 'AnotherIdThatDoesNotExistInMapping' is not present in the mapping.
		try {
			this.attributes.getAttributes(method);
			fail("Should have thrown SecureAnnotationNotInMappingException.");
		} catch (SecureAnnotationNotInMappingException expected) {
			assertTrue(true);
		}

	}

	/**
	 * Test getAttributes for method with no filter class.
	 */
	@Test
	public void getAttributesMethodClass() {
		Method method = null;

		try {
			method = MockService2.class.getMethod("someProtectedMethod");
		} catch (NoSuchMethodException unexpected) {
			fail("Should be a method called 'someProtectedMethod' on class!");
		}

		try {
			this.attributes.getAttributes(method, null);
			fail("Unsupported method should have thrown an exception!");
		} catch (UnsupportedOperationException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Test getAttributes for field.
	 */
	@Test
	public void getAttributesField() {
		try {
			Field field = null;
			this.attributes.getAttributes(field);
			fail("Unsupported method should have thrown an exception!");
		} catch (UnsupportedOperationException expected) {
			assertTrue(true);
		}
	}

	/**
	 * Test getAttributes for field and class.
	 */
	@Test
	public void getAttributesFieldClass() {
		try {
			Field field = null;
			this.attributes.getAttributes(field, null);
			fail("Unsupported method should have thrown an exception!");
		} catch (UnsupportedOperationException expected) {
			assertTrue(true);
		}
	}
}
