package no.stelvio.common.config;

import no.stelvio.common.error.FunctionalUnrecoverableException;
import no.stelvio.common.error.strategy.support.RethrowExceptionHandlerStrategy;
import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Subclasses of this exception is used to signal erroneous configuration.
 *
 * @author person983601e0e117, Accenture
 */
public abstract class ConfigurationException extends FunctionalUnrecoverableException {

    /**
	 * Constructs a copy of the specified UnrecoverableException without the cause.
     * <p>
     * Is used by the framework to make a copy for rethrowing without getting class path problems with the exception
     * classes that is part of the cause stack.
	 *
	 * @param holder
     * @see RethrowExceptionHandlerStrategy
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
