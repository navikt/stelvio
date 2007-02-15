package no.stelvio.common.security.authorization.method;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.SecurityConfig;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AnnotationAttributesMappingTest {

	@Test
	public void testAddAndGetProviders() {
		String annotationAttribute ="Id1"; 
		List<ConfigAttribute> providers = new ArrayList<ConfigAttribute>();
		providers.add(new SecurityConfig("Provider1"));
		providers.add(new SecurityConfig("Provider2"));
		providers.add(new SecurityConfig("Provider3"));
		AnnotationAttributesMapping map = new AnnotationAttributesMapping();
		map.addProviders(annotationAttribute, providers);
		assertEquals(providers.size(), map.getProviders(annotationAttribute).size());
	}

}
