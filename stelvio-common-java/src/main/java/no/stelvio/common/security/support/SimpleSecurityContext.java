package no.stelvio.common.security.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.stelvio.common.security.SecurityContext;
import no.stelvio.common.security.definition.Role;
import no.stelvio.common.security.validation.RoleNotValidException;
import no.stelvio.common.security.validation.RoleValidator;

/**
 * <code>SecurityContext</code> implementation that simply takes the userId id, the authorized as id and roles directly as
 * constructor parameters. An optional constructor also offers the possibility to validate the roles which are sent in as input
 * parameters to the class' methods.
 * 
 * @author personbf936f5cae20, Accenture
 * @author persondab2f89862d3, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: SimpleSecurityContext.java $
 * @see SecurityContext
 * @see RoleValidator
 */
public final class SimpleSecurityContext implements SecurityContext, Serializable {

	private static final long serialVersionUID = 849732249372870200L;

	private String userId;
	private String authorizedAs;
	private List<String> roles;
	private transient RoleValidator roleValidator;

	/**
	 * Constructs an instance with the given user id and list of roles.
	 * 
	 * @param userId
	 *            the logged in user's id.
	 * @param roles
	 *            the logged in user's roles.
	 */
	public SimpleSecurityContext(String userId, List<String> roles) {
		this.userId = userId;
		this.authorizedAs = userId;
		this.roles = roles;
	}

	/**
	 * Constructs an instance with the given user id, authorized as id, and list of roles.
	 * 
	 * @param userId
	 *            the logged in user's id.
	 * @param authorizedAs
	 *            the the user id of the person the user is authorized as.
	 * @param roles
	 *            the logged in user's roles.
	 */
	public SimpleSecurityContext(String userId, String authorizedAs, List<String> roles) {
		this.userId = userId;
		this.authorizedAs = authorizedAs;
		this.roles = roles;
	}

	/**
	 * Constructs an instance with the given user id, authorized as id set to the userid, list of roles and roleValidator.
	 * 
	 * @param userId
	 *            the logged in user's id.
	 * @param roles
	 *            the logged in user's roles.
	 * @param roleValidator
	 *            the validator which will be used to check if a given rolename is valid.
	 */
	public SimpleSecurityContext(String userId, List<String> roles, RoleValidator roleValidator) {
		this.userId = userId;
		this.authorizedAs = userId;
		this.roles = roles;
		this.roleValidator = roleValidator;
	}

	/**
	 * Constructs an instance with the given user id, authorized as id, list of roles and roleValidator.
	 * 
	 * @param userId
	 *            the logged in user's id.
	 * @param authorizedAs
	 *            the the user id of the person the user is authorized as.
	 * @param roles
	 *            the logged in user's roles.
	 * @param roleValidator
	 *            the validator which will be used to check if a given rolename is valid.
	 */
	public SimpleSecurityContext(String userId, String authorizedAs, List<String> roles, RoleValidator roleValidator) {
		this.userId = userId;
		this.authorizedAs = authorizedAs;
		this.roles = roles;
		this.roleValidator = roleValidator;
	}

	/**
	 * Gets the RoleValidator which is used to validate the roles that are sent in to the userInRole methods.
	 * 
	 * @return the roleValidator
	 */
	public RoleValidator getRoleValidator() {
		return roleValidator;
	}

	/**
	 * Private helper method which uses the role validator to validate the supplied roleName parameter.
	 * 
	 * @param roleName
	 *            the rolename to check.
	 */
	private void validateRole(String roleName) {
		if (roleValidator != null) {
			if (!roleValidator.isValid(roleName)) {
				throw new RoleNotValidException(roleName, roleValidator.getValidRoles(), 
						"The rolename is not a valid rolename");
			}
		}
	}

	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public String getAutorizedAs() {
		return authorizedAs;
	}

	@Override
	public List<String> getRoles() {
		return new ArrayList<>(roles);
	}

	@Override
	public boolean isUserInRole(String role) {
		validateRole(role);

		return (null != roles) && roles.contains(role);
	}

	@Override
	public boolean isUserInRole(Role role) {
		return isUserInRole(role.getRoleName());
	}

	@Override
	public boolean isUserInAllRoles(List<String> roleList) {
		boolean foundInAll = true;
		for (String role : roleList) {
			if (!isUserInRole(role)) {
				foundInAll = false;
				break;
			}
		}

		return foundInAll;
	}

	@Override
	public boolean isUserInAllRoles(String... roles) {
		boolean foundInAll = true;
		for (String role : roles) {
			if (!isUserInRole(role)) {
				foundInAll = false;
				break;
			}
		}

		return foundInAll;
	}

	@Override
	public boolean isUserInAllRoles(Role... roles) {
		boolean foundInAll = true;
		for (Role role : roles) {
			if (!isUserInRole(role)) {
				foundInAll = false;
				break;
			}
		}

		return foundInAll;
	}

	@Override
	public boolean isUserInRoles(List<String> roles) {
		boolean foundInOne = false;
		for (String role : roles) {
			if (isUserInRole(role)) {
				foundInOne = true;
				break;
			}
		}

		return foundInOne;
	}

	@Override
	public boolean isUserInRoles(String... roles) {
		boolean foundInOne = false;
		for (String role : roles) {
			if (isUserInRole(role)) {
				foundInOne = true;
				break;
			}
		}

		return foundInOne;
	}

	@Override
	public boolean isUserInRoles(Role... roles) {
		boolean foundInOne = false;
		for (Role role : roles) {
			if (isUserInRole(role)) {
				foundInOne = true;
				break;
			}
		}

		return foundInOne;
	}
}
