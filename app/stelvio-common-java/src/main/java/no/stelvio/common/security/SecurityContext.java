package no.stelvio.common.security;

import java.util.Iterator;
import java.util.List;

/**
 * Context to store security information about the logged on user.
 * 
 * @author personbf936f5cae20, Accenture
 * @author persondab2f89862d3, Accenture
 * @version $Id: SecurityContext.java $ 
 */
public final class SecurityContext {

	private String user;
	private List<String> roles;
	
	/** Default constructor */
	public SecurityContext(String user, List<String> roles) {
		this.user = user;
		this.roles = roles;
	}
	
	/**
	 * Returns the logged in user's username.
	 * 
	 * @return user name.
	 */
	public String getUser() {
		return user;
	}
	/**
	 * Returns the user's roles.
	 * 
	 * @return List of the user's roles
	 */
	public List<String> getRoles() {
		return roles;
	}	
	/**
	 * Checks if the user has one role.
	 * 
	 * @param role the rolename.
	 * @return true or false.
	 */
	public boolean isUserInRole(String role) {
		List<String> roleList = getRoles();
		return (null == roleList) ? false : roleList.contains(role);
	}
	/**
	 * Checks if the user has all the supplied roles.
	 * 
	 * @param roleList the list of roles to check.
	 * @return true if the user has all roles, false otherwise.
	 */
	public boolean isUserInAllRoles(List<String> roleList) {
		List<String> userRoles = getRoles();
		return (null == userRoles) ? false : userRoles.containsAll(roleList);
	}
	/**
	 * Checks if the user has all the supplied roles.
	 * 
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has all roles, false otherwise.
	 */
	public boolean isUserInAllRoles(String... roleparams) {
		
		List<String> userRoles = getRoles();
		Iterator<String> iter = userRoles.iterator();
		
		while (iter.hasNext()) {
			String role = iter.next();
			for (int i = 0; i < roleparams.length; i++) {
				if(!roleparams[i].equalsIgnoreCase(role)){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if the user has one of the supplied roles.
	 * 
	 * @param roleList the list of roles to check.
	 * @return true if the user has one of the roles, false otherwise.
	 */
	public boolean isUserInRoles(List<String> roleList) {
		List<String> userRoles = getRoles();
		Iterator<String> it = userRoles.iterator();
		while (it.hasNext()){
			if (userRoles.contains(it.next())){
				return true;
			}
		}
		return false;
	}	
	/**
	 * Checks if the user has one of the supplied roles.
	 * 
	 * @param roleparams the comma separated list of roles to check.
	 * @return true if the user has one of the roles, false otherwise.
	 */
	public boolean isUserInRoles(String... roleparams) {
		
		List<String> userRoles = getRoles();
		Iterator<String> iter = userRoles.iterator();
		
		while (iter.hasNext()) {
			String role = iter.next();
			for (int i = 0; i < roleparams.length; i++) {
				if(roleparams[i].equalsIgnoreCase(role)){
					return true;
				}
			}
		}
		return false;
	}
}
