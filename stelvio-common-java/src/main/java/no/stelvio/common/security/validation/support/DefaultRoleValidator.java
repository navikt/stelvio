package no.stelvio.common.security.validation.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.stelvio.common.security.definition.Role;
import no.stelvio.common.security.validation.RoleValidator;
import no.stelvio.common.security.validation.ValidRole;

/**
 * Default implementation of the RoleValidator. Offers the <code>isValid(Object role)</code> role check for the types String and
 * Role.
 * 
 * @version $Id$
 * @see RoleValidator
 */
public class DefaultRoleValidator implements RoleValidator {

	private List<ValidRole> roles;

	/**
	 * Constructs a new instance with the specified roles.
	 * 
	 * @param roles
	 *            list of roles
	 */
	public DefaultRoleValidator(List<ValidRole> roles) {
		this.roles = roles;
	}

	/**
	 * Sets the valid roles.
	 * 
	 * @param roles
	 *            the roles
	 */
	public DefaultRoleValidator(ValidRole... roles) {
		setValidRoles(roles);
	}

	@Override
	public List<ValidRole> getValidRoles() {
		return this.roles;
	}

	@Override
	public final void setValidRoles(List<ValidRole> roles) {
		this.roles = roles;
	}

	@Override
	public final void setValidRoles(ValidRole... roles) {
		this.roles = new ArrayList<>();
		this.roles.addAll(Arrays.asList(roles));
	}

	/**
	 * Checks if a role is valid. This implementation currently supports <code>String</code> or <code>Role</code> as parameters.
	 * Other types will result in a false return value.
	 * 
	 * @param role
	 *            the role to check.
	 * @return <code>true</code> if the role is valid, <code>false</code> otherwise.
	 */
	@Override
	public boolean isValid(Object role) {
		if (role instanceof String) {
			return isValid((String) role);
		} else if (role instanceof Role) {
			return isValid((Role) role);
		} else {
			return false;
		}
	}

	/**
	 * Checks if a rolename is valid represented by the String param.
	 * 
	 * @param role
	 *            the rolename to check.
	 * @return <code>true</code> if the role is valid, <code>false</code> otherwise.
	 */
	public boolean isValid(String role) {
		boolean isValidString = false;
		for (ValidRole vRole : roles) {
			if (vRole.getRoleName().equals(role)) {
				isValidString = true;
				break;
			}
		}
		return isValidString;
	}

	/**
	 * Checks if a rolename is valid represented by the Role param.
	 * 
	 * @param role
	 *            the role to check.
	 * @return <code>true</code> if the role is valid, <code>false</code> otherwise.
	 */
	public boolean isValid(Role role) {
		boolean isValidRole = false;
		for (ValidRole vRole : roles) {
			if (vRole.getRoleName().equals(role.getRoleName())) {
				isValidRole = true;
				break;
			}
		}
		return isValidRole;
	}
}
