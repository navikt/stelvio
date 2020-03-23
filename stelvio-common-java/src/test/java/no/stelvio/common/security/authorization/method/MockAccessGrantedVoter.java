package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Mocks AccessGrantedVoter.
 * 
 *
 */
public class MockAccessGrantedVoter implements AccessDecisionVoter<Object> {

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

    /**
	 * {@inheritDoc}
	 */
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) {
		return ACCESS_GRANTED;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(Class clazz) {
		return true;
	}

}