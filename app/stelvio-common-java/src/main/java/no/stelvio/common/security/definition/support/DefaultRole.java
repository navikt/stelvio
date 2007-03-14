package no.stelvio.common.security.definition.support;

import no.stelvio.common.security.definition.Role;

/**
 * Default implementation of a Role which simply contains the name of the role.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see Role
 */
public class DefaultRole implements Role {
	/** The rolename of this role */
	private String roleName;
	
	/**
	 * Constructs a role with the given rolename.
	 * @param roleName the name of the role.
	 */
	public DefaultRole(String roleName){
		this.roleName = roleName;
	}
	/**
	 * {@inheritDoc}
	 */
	public String getRoleName() {
		return this.roleName;
	}
}
