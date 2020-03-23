package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Mocks AfterInvocationProviderNoFiltering.
 * 
 *
 */
public class MockAfterInvocationProviderNoFiltering implements AfterInvocationProvider {

	/**
	 * {@inheritDoc}
	 */
	public Object decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes, Object returnedObject)
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
