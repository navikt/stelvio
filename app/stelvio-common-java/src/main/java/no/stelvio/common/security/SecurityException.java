package no.stelvio.common.security;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * Thrown to indicate that an unrecoverable security exception has occured. <p/>
 * Applications will typically not handle recovery from these exceptions.
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public abstract class SecurityException extends SystemUnrecoverableException {

	/**
	 * Constructs a new SecurityException with the specified cause and list of
	 * templateArguments for the messageFrom template.
	 * 
	 * @param cause
	 *            the cause of this exception.
	 * @param templateArguments
	 *            the templateArguments to use when filling out the messageFrom
	 *            template.
	 */
	protected SecurityException(Throwable t, Object... templateArguments) {
		super(t, templateArguments);
	}

	/**
	 * Constructs a new SecurityException with the specified list of
	 * templateArguments for the messageFrom template.
	 * 
	 * @param templateArguments
	 *            the templateArguments to use when filling out the messageFrom
	 *            template.
	 */
	protected SecurityException(Object... templateArguments) {
		super(templateArguments);
	}
}
