package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

/**
 * Mocks AfterInvocationProviderDeniesAccess.
 * 
 * @author ??
 * 
 */
public class MockAfterInvocationProviderDeniesAccess implements AfterInvocationProvider {

	/**
	 * {@inheritDoc}
	 */
	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject)
			throws MethodAccessDeniedException {
		throw new MethodAccessDeniedException("Access is denied");

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
