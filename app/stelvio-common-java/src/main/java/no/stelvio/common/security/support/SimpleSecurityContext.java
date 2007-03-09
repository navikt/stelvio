package no.stelvio.common.security.support;

import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.RoleName;
import no.stelvio.common.security.SecurityContext;

/**
 * <code>SecurityContext</code> implementation that simply takes the userId id and roles directly as constructor
 * parameters.
 *
 * @author personbf936f5cae20, Accenture
 * @author persondab2f89862d3, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: SecurityContext.java $ 
 * @see SecurityContext
 */
public final class SimpleSecurityContext implements SecurityContext {
	private String userId;
	private List<String> roles;
	
	/**
	 * Constructs an instance with the given user id an list of roles.
	 *
	 * @param userId the logged in user's id.
	 * @param roles the logged in user's roles.
	 */
	public SimpleSecurityContext(String userId, List<String> roles) {
		this.userId = userId;
		this.roles = roles;
	}
	/**
	 * {@inheritDoc}
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRole(String role) {
		List<String> roleList = getRoles();

		return (null != roleList) && roleList.contains(role);
	}
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInAllRoles(List<String> roleList) {
		List<String> userRoles = getRoles();

		return (null != userRoles) && userRoles.containsAll(roleList);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInAllRoles(String... roleparams) {
		for (String roleparam : roleparams) {
			if (!isUserInRole(roleparam)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRoles(List<String> roleList) {
		for (String userRole : roleList) {
			if(isUserInRole(userRole)){
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isUserInRoles(String... roleparams) {
			for (String roleparam : roleparams) {
				if (isUserInRole(roleparam)) {
					return true;
				}
			}
		return false;
	}
}
