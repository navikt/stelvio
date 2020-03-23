package no.stelvio.dto.exception.mapping;

/**
 * Signals that a mapping DTO exception has occurred.
 * 
 */
public class MappingDtoException extends AbstractMappingDtoException {

	private static final long serialVersionUID = -1855978243125358226L;

	/**
	 * Constructs a <code>MappingDtoException</code> with message and cause.
	 * 
	 * @param message - the exception message.
	 * @param cause - the throwable that caused the exception to be raised.
	 */	
	public MappingDtoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a <code>MappingDtoException</code> with message.
	 * 
	 * @param message - the exception message.
	 */	
	public MappingDtoException(String message) {
		super(message);
	}
}