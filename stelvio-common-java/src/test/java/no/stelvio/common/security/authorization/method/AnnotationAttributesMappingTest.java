package no.stelvio.common.security.authorization.method;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

/**
 * AnnotationAttributesMappingTest.
 * 
 * @author ??
 * 
 */
public class AnnotationAttributesMappingTest {

	/**
	 * Test add and get providers.
	 */
	@Test
	public void testAddAndGetProviders() {
		String annotationAttribute = "Id1";
		List<ConfigAttribute> providers = new ArrayList<ConfigAttribute>();
		providers.add(new SecurityConfig("Provider1"));
		providers.add(new SecurityConfig("Provider2"));
		providers.add(new SecurityConfig("Provider3"));
		AnnotationAttributesMapping map = new AnnotationAttributesMapping();
		map.addProviders(annotationAttribute, providers);
		assertEquals(providers.size(), map.getProviders(annotationAttribute).size());
	}

}
