package no.stelvio.common.config;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Subclasses of this exception is used to signal erroneous configuration.
 *
 * @author person983601e0e117, Accenture
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
