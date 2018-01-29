package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Mocks AccessDecisionVoter.
 * 
 * @author ??
 * 
 */
public class MockAccessAbstainVoter implements AccessDecisionVoter<Object> {

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
		return ACCESS_ABSTAIN;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(Class clazz) {
		return true;
	}

}