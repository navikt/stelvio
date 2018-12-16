package no.stelvio.presentation.security.context.parse;

import no.stelvio.common.security.definition.Role;

/**
 * This class represent a security role from web.xml.
 *
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class SecurityRole implements Role {
	/** The name of the security role. */
	private String roleName;

	/**
	 * Gets the name of the security role.
	 *
	 * @return the roleName.
	 */
	@Override
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Sets the name of the security role.
	 *
	 * @param roleName the roleName to set.
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * Returns a String object representing the roleName.
	 *
	 * @return a String object representing the roleName..
	 */
	@Override
	public String toString() {
		return roleName;
	}
}
