package no.stelvio.common.security.authorization.method;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.SecurityConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class SecurityAnnotationsAttributesTest {

	private SecurityAnnotationsAttributes attributes;
	
	@Before
	public void setUp() throws Exception {
//		 create the Annotations support
        this.attributes = new SecurityAnnotationsAttributes();
        setUpMapping();
	}
	private void setUpMapping(){
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
        map.addProviders("Id1",id1);
        map.addProviders("Id2",id2);
        map.addProviders("Id3",id3);
        attributes.setAnnotationMapping(map);    
	}
	

	@Test
	public void testGetAttributesClass() {
		Collection<ConfigAttribute> attrs = this.attributes.getAttributes(MockService.class);
        assertNotNull(attrs);
        // expect 1 annotation mapped to 2 providers
        assertTrue(attrs.size() == 2);
        Collection<ConfigAttribute> providers = this.attributes.getAnnotationMapping().getProviders("Id1");
        assertNotNull(providers);
        assertEquals(providers,attrs);
	}
	@Test
	public void testGetAttributesClassWithAnnotationAttributeNotPresentInMapping() {
		//Class is annotated with @Secured({"IdThatDoesNotExistInMapping"}) which is not present in the mapping
		try {
				this.attributes.getAttributes(MockService2.class);
				fail("Should have thrown SecureAnnotationNotInMappingException.");
		} catch (SecureAnnotationNotInMappingException expected) {
			assertTrue(true);
		}	
	}

	@Test
	public void testGetAttributesClassClass() {
		
		try {
            this.attributes.getAttributes(MockService.class, null);
            fail("Unsupported method should have thrown an exception!");
        } catch (UnsupportedOperationException expected) {
        	assertTrue(true);
        }
	}
	
	
	
	@Test
	public void testGetAttributesMethod() {
		
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
	
	@Test
	public void testGetAttributesMethodWithAnnotationAttributeNotPresentInMapping() {
		Method method = null;

        try {
            method = MockService2.class.getMethod("someProtectedMethod");
        } catch (NoSuchMethodException unexpected) {
            fail("Should be a method called 'someProtectedMethod' on class!");
        }   
        //Method is annotated with @Secured({"Id2","AnotherIdThatDoesNotExistInMapping"}) 
        //where 'AnotherIdThatDoesNotExistInMapping' is not present in the mapping.
		try {
				this.attributes.getAttributes(method);
				fail("Should have thrown SecureAnnotationNotInMappingException.");
		} catch (SecureAnnotationNotInMappingException expected) {
			assertTrue(true);
		}	
        
	}
	@Test
	public void testGetAttributesMethodClass() {
		Method method = null;

        try {
            method = MockService2.class.getMethod("someProtectedMethod");
        } catch (NoSuchMethodException unexpected) {
            fail("Should be a method called 'someProtectedMethod' on class!");
        }

        try {
            this.attributes.getAttributes(method, null);
            fail("Unsupported method should have thrown an exception!");
        } catch (UnsupportedOperationException expected) {}
	}

	@Test
	public void testGetAttributesField() {
		try {
            Field field = null;
            this.attributes.getAttributes(field);
            fail("Unsupported method should have thrown an exception!");
        } catch (UnsupportedOperationException expected) {}
	}

	@Test
	public void testGetAttributesFieldClass() {
		try {
            Field field = null;
            this.attributes.getAttributes(field, null);
            fail("Unsupported method should have thrown an exception!");
        } catch (UnsupportedOperationException expected) {}
	}
}
