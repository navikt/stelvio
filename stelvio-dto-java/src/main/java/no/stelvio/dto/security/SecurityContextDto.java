package no.stelvio.dto.security;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object representation of <code>no.stelvio.common.security.SecurityContext</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 */
public class SecurityContextDto implements Serializable {

	private static final long serialVersionUID = 3211042935264797058L;

	private String userId;
	
	private String[] roles;

	/**
	 * Returns the String representation of this SecurityContextDto object.
	 * @return this objects String representation
	 */
	@Override
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
		this.roles = roles.clone();
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
