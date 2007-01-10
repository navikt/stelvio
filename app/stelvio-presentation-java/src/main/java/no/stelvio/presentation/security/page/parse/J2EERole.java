package no.stelvio.presentation.security.page.parse;

import java.io.Serializable;

/**
 * Object representing one J2EE role.
 * 
 * @author persondab2f89862d3
 * @version $Id$
 */
public class J2EERole implements Serializable {

	private static final long serialVersionUID = 1L;

	private String role;

	public J2EERole() {
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
	 */
	@Override
	public String toString() {
		return this.role;
	}
}
