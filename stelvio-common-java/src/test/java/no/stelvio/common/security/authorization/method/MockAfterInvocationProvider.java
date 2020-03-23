package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Mocks AfterInvocationProvider.
 * 
 *
 */
public class MockAfterInvocationProvider implements AfterInvocationProvider {

	/**
	 * {@inheritDoc}
	 */
    public Object decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes, Object o2) throws AccessDeniedException {
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
