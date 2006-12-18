package no.stelvio.common.config;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exceptions thrown by the init-method in Stelvio configuration 
 * as a result of erroneous configuration are subclasses of this exception
 * @author person983601e0e117, Accenture
 *
 */
public abstract class ConfigurationException extends FunctionalUnrecoverableException {

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException#FunctionalUnrecoverableException(ExceptionToCopyHolder)}
	 */	
	public ConfigurationException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException#FunctionalUnrecoverableException(Object...)}
	 */
	public ConfigurationException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * {@inheritDoc no.stelvio.common.error.FunctionalUnrecoverableException#FunctionalUnrecoverableException(Throwable, Object...)}
	 */
	public ConfigurationException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}


}
