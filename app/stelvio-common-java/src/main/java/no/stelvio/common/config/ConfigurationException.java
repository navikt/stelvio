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
	 * Constructs a copy of the specified ConfigurationException without the cause.
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
	 * Constructs a new Configuration exception with the specified templatearguments
	 * @param templateArguments - list of arguments
	 */
	public ConfigurationException(Object... templateArguments) {
		super(templateArguments);
	}

	/**
	 * Constructs a new Configuration exception with the specified templatearguments
	 * @param cause - the <code>Throwable</code> cause of this exception
	 * @param templateArguments - list of arguments
	 */
	public ConfigurationException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}


}
