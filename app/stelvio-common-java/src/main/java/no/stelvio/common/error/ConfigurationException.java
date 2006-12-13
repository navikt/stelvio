package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exceptions thrown by the init-method in Stelvio configuration 
 * as a result of erroneous configuration are subclasses of this exception
 * @author person983601e0e117, Accenture
 *
 */
public abstract class ConfigurationException extends UnrecoverableException {

	/**
	 * {@inheritDoc no.stelvio.common.error.UnrecoverableException#UnrecoverableException(ExceptionToCopyHolder)}
	 */	
	public ConfigurationException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.UnrecoverableException#UnrecoverableException(Object...)}
	 */
	public ConfigurationException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.UnrecoverableException#UnrecoverableException(Throwable, Object...)}
	 */
	public ConfigurationException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}


}
