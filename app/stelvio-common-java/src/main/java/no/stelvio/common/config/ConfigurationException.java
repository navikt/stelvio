package no.stelvio.common.config;

import no.stelvio.common.error.SystemException;

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
	 * @param argument detail to be included in the error message.
	 */
	public ConfigurationException(Object argument) {
		this(null, argument);
	}

	/**
	 * Constructs a new ConfigurationException with the specified error code, cause and argument.
	 *
	 * @param cause the cause of this exception.
     * @param argument detail to be included in the error message.
     */
	public ConfigurationException(Throwable cause, Object argument) {
		super(cause, argument);
	}

    /**
     * @todo not quite correct
     */
    public Object copy() {
        return new ConfigurationException(null);
    }
}
