package no.stelvio.common.security.validation;

import java.util.List;

import no.stelvio.common.security.SecurityException;

/**
 * Exception thrown if the validation of a rolename using a list of <code>ValidRole</code>s fails.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 * @see ValidRole
 */
public class RoleNotValidException extends SecurityException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param cause
	 * 			  the root cause for the exception.
	 * @param roleName the rolename up for validation.
	 * @param validRoles the list of valid roles used to validate the rolename.  
	 */
	public RoleNotValidException(Throwable cause, String roleName, List<ValidRole> validRoles) {		
		super(cause, roleName, validRoles);
	}
	
	/**
	 * Creates an exception with the supplied parameters in the message.
	 * 
	 * @param roleName the rolename up for validation.
	 * @param validRoles the list of valid roles used to validate the rolename.  
	 */
	public RoleNotValidException(String roleName, List<ValidRole> validRoles) {
		super(roleName, validRoles);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String messageTemplate(int numArgs) {
		
		return "The rolename {0} is not a valid rolename, see {1} for all the valid rolenames.";
	}

}
