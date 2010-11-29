package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

/**
 * Mocks AccessGrantedVoter.
 * 
 * @author ??
 * 
 */
public class MockAccessGrantedVoter implements AccessDecisionVoter {

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public int vote(Authentication authentication, Object object, ConfigAttributeDefinition config) {
		return ACCESS_GRANTED;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean supports(Class clazz) {
		return true;
	}

}