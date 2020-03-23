package no.stelvio.dto.exception.mapping;

import no.stelvio.dto.exception.FunctionalUnrecoverableDtoException;

/**
 * Signals that an abstract mapping DTO exception has occurred. 
 * 
 */
public abstract class AbstractMappingDtoException extends FunctionalUnrecoverableDtoException {
	
	/**
	 * Constructs a <code>AbstractMappingDtoException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */	
	protected AbstractMappingDtoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a <code>AbstractMappingDtoException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	protected AbstractMappingDtoException(String message) {
		super(message);
	}

}
