package no.stelvio.common.security.authorization.method;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

/**
 * Mocks AfterInvocationProviderNoFiltering.
 * 
 * @author ??
 * 
 */
public class MockAfterInvocationProviderNoFiltering implements AfterInvocationProvider {

	/**
	 * {@inheritDoc}
	 */
	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject)
			throws AccessDeniedException {
		return returnedObject;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(Class clazz) {
		return true;
	}
}
