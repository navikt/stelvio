package no.stelvio.presentation.security.page.definition.parse.support;

import java.io.Serializable;

/**
 * Object representing one J2EE role.
 * 
 * @version $Id$
 */
public class JeeRole implements Serializable {

	private static final long serialVersionUID = 1L;

	private String role;

	/**
	 * Default constructor for class.
	 *
	 */
	public JeeRole() {
	}

	/**
	 * Sets the role name.
	 * 
	 * @param role
	 *            the role name
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Gets the role name.
	 * 
	 * @return role name.
	 */
	public String getRole() {
		return this.role;
	}

	/**
	 * Returns string representation of this class.
	 * @return String representation of this class
	 */
	@Override
	public String toString() {
		return this.role;
	}
}
