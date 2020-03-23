package no.stelvio.common.security.validation;

import java.util.List;

/**
 * Interface for defining functionality to check if a security role is valid.
 *  
 * @version $Id$
 * @see ValidRole
 */
public interface RoleValidator {
	
	/**
	 * Sets a list of ValidRole's for this validator to check against.
	 * @param roles the list of valid roles.
	 */
	void setValidRoles(List<ValidRole> roles);
	
	/**
	 * Sets a list of ValidRole's for this validator to check against.
	 * @param roles the array or commaseparated list of valid roles.
	 */
	void setValidRoles(ValidRole... roles);
	
	/**
	 * Gets the valid roles registered with this validator.
	 * @return the list of valid roles.
	 */
	List<ValidRole> getValidRoles();
	
	/**
	 * Checks if a role is valid.
	 * @param role the role to check.
	 * @return <code>true</code> if the role is valid, <code>false</code> otherwise.
	 */
	boolean isValid(Object role);
}
