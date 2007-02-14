package no.stelvio.batch.exception;

import no.stelvio.common.error.SystemUnrecoverableException;
/**
 * Superclass for all SystemUnrecoverableExceptions thrown from batches
 * @author person983601e0e117 (Accenture)
 *
 */
public abstract class BatchException extends SystemUnrecoverableException {

    /**
	 * Constructs a new BatchException with the specified list of templateArguments for the messageFrom template.
	 * 
	 * @param templateArguments the templateArguments to use when filling out the messageFrom template.
     */
	public BatchException(Object... templateArguments) {
		super(templateArguments);
	}

    /**
	 * Constructs a new BatchException with the specified list of templateArguments for the messageFrom template.
	 * 
	 * @param templateArguments the templateArguments to use when filling out the messageFrom template.
	 * @param cause the underlying cause for this exception
     */
	public BatchException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}


}
