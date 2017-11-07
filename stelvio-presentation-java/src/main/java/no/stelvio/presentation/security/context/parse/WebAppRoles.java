package no.stelvio.presentation.security.context.parse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represent a collection of security roles from web.xml.
 * 
 * @see SecurityRole
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class WebAppRoles {

	private List<SecurityRole> securityRoles = new ArrayList<SecurityRole>();

	/**
	 * Adds a SecurityRole to the collection.
	 * 
	 * @param securityRole
	 *            the security role to add
	 */
	public void addSecurityRole(SecurityRole securityRole) {
		securityRoles.add(securityRole);
	}

	/**
	 * Gets the collection of security roles.
	 * 
	 * @return all the security roles
	 */
	public List<SecurityRole> getSecurityRoles() {
		return securityRoles;
	}

	/**
	 * Gets the iterator for the security role collection.
	 * 
	 * @return the security roles iterator
	 */
	public Iterator<SecurityRole> getSecurityRolesIterator() {
		return securityRoles.iterator();
	}

}
