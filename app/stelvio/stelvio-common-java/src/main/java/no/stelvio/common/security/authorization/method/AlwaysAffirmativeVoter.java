package no.stelvio.common.security.authorization.method;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

/**
 * Implementation of a <code>AccessDecisionVoter</code> that always grants
 * access to a secure object.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AlwaysAffirmativeVoter implements AccessDecisionVoter {

	/**
	 * This implementation supports any type of ConfigAttribute, because it does
	 * not use the presented attribute.
	 * 
	 * @param attribute
	 *            the configuration attribute
	 * 
	 * @return always <code>true</code>
	 */
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	/**
	 * This implementation supports any type of class, because it does not query
	 * the presented secure object.
	 * 
	 * @param clazz
	 *            the secure object
	 * 
	 * @return always <code>true</code>
	 */
	public boolean supports(Class clazz) {
		return true;
	}

	/**
	 * This implementation always grant access to a secure object.
	 * @param authentication {@inheritDoc}
	 * @param object {@inheritDoc}
	 * @param config {@inheritDoc}
	 * @return always
	 *         <code>org.acegisecurity.vote.AccessDecisionVoter.ACCESS_GRANTED</code>
	 * @see org.acegisecurity.vote.AccessDecisionVoter#vote(org.acegisecurity.Authentication,
	 *      java.lang.Object, org.acegisecurity.ConfigAttributeDefinition)
	 */
	public int vote(Authentication authentication, Object object,
			ConfigAttributeDefinition config) {
		return ACCESS_GRANTED;
	}
}
