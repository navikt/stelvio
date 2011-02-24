package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.afterinvocation.AfterInvocationProvider;

/**
 * Mocks AfterInvocationProvider.
 * 
 * @author ??
 * 
 */
public class MockAfterInvocationProvider implements AfterInvocationProvider {

	/**
	 * {@inheritDoc}
	 */
	public Object decide(Authentication authentication, Object object, ConfigAttributeDefinition config, Object returnedObject)
			throws MethodAccessDeniedException {
		String filteredValue = "Filtered";
		return filteredValue;
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
