package no.stelvio.dto.security;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Data Transfer Object representation of <code>no.stelvio.common.security.SecurityContext</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * @author person983601e0e117 (Accenture)
 * @author person6045563b8dec (Accenture)
 */
public class SecurityContextDto implements Serializable {


	private static final long serialVersionUID = 3211042935264797058L;

	private String userId;
	
	private String[] roles;

	/**
	 * Returns the String representation of this SecurityContextDto object.
	 * @return this objects String representation
	 */
	public String toString() {
		return new ToStringBuilder(this).
					append("userId", userId).
					append("roles", roles).toString();
	}
	
	/**
	 * Gets the roles.
	 * 
	 * @return roles the roles
	 */	
	public String[] getRoles() {
		return roles;
	}
	
	/**
	 * Sets the roles.
	 * 
	 * @param roles the roles
	 */
	public void setRoles(String[] roles) {
		this.roles = roles;
	}

	/**
	 * Gets the userid.
	 * 
	 * @return userId the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userid.
	 * 
	 * @param userId the userId
	 */	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
