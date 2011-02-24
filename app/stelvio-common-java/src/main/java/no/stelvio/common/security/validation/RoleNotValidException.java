package no.stelvio.common.security.validation;

import java.util.List;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if the validation of a rolename using a list of <code>ValidRole</code>s fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @see ValidRole
 */
public class RoleNotValidException extends SecurityException {

	private static final long serialVersionUID = 1L;

	private List<ValidRole> validRoles;

	private String roleName;

	/**
	 * Constructs a <code>RoleNotValidException</code> with and message.
	 * 
	 * @param roleName -
	 *            the invalid role name.
	 * @param validRoles -
	 *            a list of the valid roles.
	 * @param message -
	 *            the exception message.
	 */
	public RoleNotValidException(String roleName, List<ValidRole> validRoles, String message) {
		super(message);
		this.roleName = roleName;
		this.validRoles = validRoles;
	}

	/**
	 * Constructs a <code>RoleNotValidException</code> with message and cause.
	 * 
	 * @param message -
	 *            the exception message.
	 * @param cause -
	 *            the throwable that caused the exception to be raised.
	 */
	public RoleNotValidException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a <code>RoleNotValidException</code> with message.
	 * 
	 * @param message -
	 *            the exception message.
	 */
	public RoleNotValidException(String message) {
		super(message);
	}

	/**
	 * Get role name.
	 * 
	 * @return role name
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Get valid roles.
	 * 
	 * @return valid roles
	 */
	public List<ValidRole> getValidRoles() {
		return validRoles;
	}

}