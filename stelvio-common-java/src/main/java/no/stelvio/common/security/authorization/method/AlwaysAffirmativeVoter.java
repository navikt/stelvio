package no.stelvio.common.security.authorization.method;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Implementation of a <code>AccessDecisionVoter</code> that always grants
 * access to a secure object.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AlwaysAffirmativeVoter implements AccessDecisionVoter<Object> {

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
    */
    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) {
        return ACCESS_GRANTED;
    }
}
