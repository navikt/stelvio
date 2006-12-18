package no.stelvio.common.error;

import no.stelvio.common.error.support.ExceptionToCopyHolder;

/**
 * Exceptions thrown by the init-method in Stelvio configuration are subclasses of this exception
 * @author person983601e0e117, Accenture
 *
 */
public abstract class InitMethodException extends UnrecoverableException {

	
	public InitMethodException(ExceptionToCopyHolder holder) {
		super(holder);
	}

	public InitMethodException(Object... templateArguments) {
		super(templateArguments);
	}

	public InitMethodException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}


}
