package no.stelvio.common.security;

import java.util.List;

/**
 * Context that has security information about the logged on user. Implementations decide how to get to/store the
 * information necessary for providing this functionality.
 *
 * @author personbf936f5cae20, Accenture
 * @author persondab2f89862d3, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: SecurityContext.java $
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
	 * Checks if the user has one role.
	 *
	 * @param role the rolename.
	 * @return true or false.
	 */
	boolean isUserInRole(String role);
	
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
}
