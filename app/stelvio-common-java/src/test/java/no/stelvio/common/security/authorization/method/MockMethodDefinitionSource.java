package no.stelvio.common.security.authorization.method;

import java.util.Iterator;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.method.MethodDefinitionSource;

public class MockMethodDefinitionSource implements MethodDefinitionSource {

	public ConfigAttributeDefinition definition;
	
	public ConfigAttributeDefinition getAttributes(Object object)
			throws IllegalArgumentException {
		return definition;
	}

	public Iterator getConfigAttributeDefinitions() {
		return null;
	}

	public boolean supports(Class clazz) {
		return true;
	}

}
