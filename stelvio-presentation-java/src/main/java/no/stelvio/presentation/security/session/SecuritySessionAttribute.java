package no.stelvio.presentation.security.session;

import no.stelvio.presentation.security.page.constants.Constants;

/**
 * The security session attributes.
 * 
 * @version $Id$
 */
public enum SecuritySessionAttribute {
	/** Security phaselistener exception. */
	SECURITY_PHASELISTENER_EXCEPTION,

	/** Security context user id. */
	SECURITYCONTEXT_USERID, 
	
	/** Security context user roles. */
	SECURITYCONTEXT_USER_ROLES,

	/** EAI original user id. */
	EAI_ORIGINAL_USER_ID, 
	
	/** EAI authentication level. */
	EAI_AUTHENTICATION_LEVEL, 
	
	/** EAI url upon successfuul stepup. */
	EAI_URL_UPON_SUCCESSFUL_STEPUP,

	/** Jsf page to go after authentication. */
	JSFPAGE_TOGO_AFTER_AUTHENTICATION(Constants.JSFPAGE_TOGO_AFTER_AUTHENTICATION),

	/** Authentication level change request. */
	AUTHENTICATION_LEVEL_CHANGE_REQUEST;

	private String newName;

	/**
	 * Creates a new instance of SecuritySessionAttribute.
	 */
	SecuritySessionAttribute() {
	}

	/**
	 * Creates a new instance of SecuritySessionAttribute.
	 *
	 * @param changeName name
	 */
	SecuritySessionAttribute(String changeName) {
		this.newName = changeName;
	}

	/**
	 * Get name.
	 * 
	 * @return name
	 */
	public String getName() {
		if (this.newName == null) {
			return name();
		} else {
			return newName;
		}
	}
}
