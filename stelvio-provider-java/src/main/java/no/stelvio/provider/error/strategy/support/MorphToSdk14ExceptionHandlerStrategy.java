package no.stelvio.provider.error.strategy.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.error.strategy.support.AbstractOrderedExceptionHandlerStrategy;
import no.stelvio.dto.error.strategy.support.ImitatorDtoException;
import no.stelvio.dto.exception.RecoverableDtoException;
import no.stelvio.dto.exception.UnrecoverableDtoException;

/**
 * Morpher for use in the provider layer to convert from Java 5.0 SE code to Java 1.4 SE.
 * 
 *
 */
public class MorphToSdk14ExceptionHandlerStrategy extends AbstractOrderedExceptionHandlerStrategy {

	private static final Log LOG = LogFactory.getLog(MorphToSdk14ExceptionHandlerStrategy.class);

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Throwable> T handleException(T exception) {

		// If RecoverableDtoException subclass, it has already been transformed.
		// Return without morphing
		if (exception instanceof RecoverableDtoException) {
			return exception;
		} else if (exception instanceof UnrecoverableDtoException) {
			// This is included as long as the existence of UnrecoverableDtoException is under consideration
			return exception;
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("About to start imitating Throwable " + exception.getClass().getName() + ".");
		}

		T copy = (T) new ImitatorDtoException(exception);

		Throwable origCopy = copy;
		Throwable cause = exception.getCause();

		for (; cause != null; cause = cause.getCause()) {
			ImitatorDtoException imitator = new ImitatorDtoException(cause);
			origCopy.initCause(imitator);
			origCopy = imitator;
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug(exception.getClass().getName() + " was imitated. "
					+ "Will be returned as an instance of ImitatorDtoException same stacktrace as original.");
		}

		return copy;
	}

}
