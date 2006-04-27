package no.nav.common.framework.config;

import no.nav.common.framework.error.ErrorCode;
import no.nav.common.framework.error.SystemException;

/**
 * Thrown to indicate that an exception occured in the configuration service.
 * 
 * @author person7553f5959484
 * @version $Revision: 2153 $ $Author: skb2930 $ $Date: 2005-03-31 14:39:03 +0200 (Thu, 31 Mar 2005) $
 */
public class ConfigurationException extends SystemException {

	/**
	 * Constructs a new ConfigurationException with the specified error code and argument.
	 * 
	 * @param code the error code to be used when handling the exception.
	 * @param argument detail to be included in the error message.
	 */
	public ConfigurationException(ErrorCode code, Object argument) {
		this(code, null, argument);
	}

	/**
	 * Constructs a new ConfigurationException with the specified error code, cause and argument.
	 *
	 * @param code the error code to be used when handling the exception.
	 * @param cause the cause of this exception.
	 * @param argument detail to be included in the error message.
	 */
	public ConfigurationException(ErrorCode code, Throwable cause, Object argument) {
		super(code, cause, argument);
	}
}
