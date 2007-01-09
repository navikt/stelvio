package no.stelvio.common.security.authorization.method;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import no.stelvio.common.security.authorization.method.AnnotationAttributesMapping;

import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.SecurityConfig;
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
