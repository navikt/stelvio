package no.stelvio.common.security.authorization.method;

import java.util.Iterator;

import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.method.MethodDefinitionSource;

/**
 * Mocks MethodDefinitionSource.
 * 
 * @author ??
 * 
 */
public class MockMethodDefinitionSource implements MethodDefinitionSource {

	private ConfigAttributeDefinition definition;

	/**
	 * {@inheritDoc}
	 */
	public ConfigAttributeDefinition getAttributes(Object object) {
		return definition;
	}

	/**
	 * {@inheritDoc}
	 */
	public Iterator getConfigAttributeDefinitions() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(Class clazz) {
		return true;
	}

	/**
	 * Sets the definition.
	 *
	 * @param definition the definition to set
	 */
	public void setDefinition(ConfigAttributeDefinition definition) {
		this.definition = definition;
	}

}
