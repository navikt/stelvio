package no.stelvio.common.security;

import java.util.List;

import no.stelvio.common.security.definition.Role;


/**
 * Context that has security information about the logged on user. Implementations decide how to get to/store the
 * information necessary for providing this functionality.
 *
 * @author personbf936f5cae20, Accenture
 * @author personf8e9850ed756, Accenture
 * @author persondab2f89862d3, Accenture
 * @version $Id: SecurityContext.java $
 * @see Role
 */
public interface SecurityContext {
	/**
	 * Returns the logged in user's id.
	 *
	 * @return logged in user's id.
	 */
	String getUserId();

	/**
	 * Returns the user's roles.
	 *
	 * @return List of the user's roles.
	 */
	List<String> getRoles();
	
	/**
	 * Returns the user id of the person the user is authorized as.
	 * 
	 * @return user id of the person the user is authorized as.
	 */
	String getAutorizedAs();
	
	/**
	 * Checks if the user has one role.
	 *
	 * @param role the rolename.
	 * @return true or false.
	 */
	boolean isUserInRole(String role);
	
	/**
	 * Checks if the user has one role.
	 *
	 * @param role the role to check.
	 * @return true or false.
	 */
	boolean isUserInRole(Role role);
	
	/**
	 * Checks if the user has all the supplied roles.
	 *
	 * @param roleList the list of roles to check.
	 * @return true if the user has all roles, false otherwise.
	 */
	boolean isUserInAllRoles(List<String> roleList);

	
	/**
	 * Checks if the user has all the supplied roles.
	 *
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has all roles, false otherwise.
	 */
	boolean isUserInAllRoles(String... roleparams);
	
	/**
	 * Checks if the user has all the supplied roles.
	 *
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has all roles, false otherwise.
	 */
	boolean isUserInAllRoles(Role... roleparams);

	/**
	 * Checks if the user has one of the supplied roles.
	 *
	 * @param roleList the list of roles to check.
	 * @return true if the user has one of the roles, false otherwise.
	 */
	boolean isUserInRoles(List<String> roleList);

	/**
	 * Checks if the user has one of the supplied roles.
	 *
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has one of the roles, false otherwise.
	 */
	boolean isUserInRoles(String... roleparams);
	
	/**
	 * Checks if the user has one of the supplied roles.
	 *
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has one of the roles, false otherwise.
	 */
	boolean isUserInRoles(Role... roleparams);

	/**
	 * Return an attribute defined in the attributeMap or null
	 */
	public String getAttribute(String key);
}
