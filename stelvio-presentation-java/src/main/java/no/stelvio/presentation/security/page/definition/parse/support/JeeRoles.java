package no.stelvio.presentation.security.page.definition.parse.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import no.stelvio.presentation.security.page.constants.Constants;

/**
 * Object representing a list of J2EE roles required for a page authorization.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class JeeRoles implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//private List<JeeRole> roles = null;
	private ArrayList<JeeRole> roles = null;

	private String roleConcatinationType = Constants.J2EE_ROLE_CONCATINATION_AND;

	/**
	 * Add a role to the list.
	 * 
	 * @param role
	 *            the role to add.
	 */
	public void addRole(JeeRole role) {
		if (this.roles == null) {
			this.roles = new ArrayList<JeeRole>();
		}
		// add role name to list of roles
		this.roles.add(role);
	}

	/**
	 * Set a list of roles.
	 * 
	 * @param roles
	 *            the list of roles to set.
	 */
	public void setRoles(List<JeeRole> roles) {
		if (this.roles == null) {
			this.roles = new ArrayList<JeeRole>();
		}
		// add all roles from the input List "roles"
		this.roles.addAll(roles);
	}

	/**
	 * Get the list of roles.
	 * 
	 * @return the role list.
	 */
	public List<JeeRole> getRoles() {
		return this.roles;
	}

	/**
	 * Sets how the roles should be concatenated, i.e. using either AND or OR.
	 * 
	 * @param roleConcatenation
	 *            <br>
	 *            Values are:
	 * 
	 * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_OR</code>,
	 * <code>no.stelvio.web.security.page.Constant.J2EE_ROLE_CONCATINATION_AND</code>
	 */
	public void setRoleConcatenationType(String roleConcatenation) {
		if (roleConcatenation
				.equalsIgnoreCase(Constants.J2EE_ROLE_CONCATINATION_AND)) {
			this.roleConcatinationType = Constants.J2EE_ROLE_CONCATINATION_AND;
		} else {
			this.roleConcatinationType = Constants.J2EE_ROLE_CONCATINATION_OR;
		}
	}

	/**
	 * Returns OR or AND based on how J2EE roles should be concatinated for
	 * security evaluation.
	 * 
	 * @return "OR" or "AND"
	 */
	public String getRoleConcatenationType() {
		return this.roleConcatinationType;
	}

	/**
	 * Returns string representation of this class.
	 * @return a string representation of this class.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append(":").append(this.roles);
		return sb.toString();
	}

}
