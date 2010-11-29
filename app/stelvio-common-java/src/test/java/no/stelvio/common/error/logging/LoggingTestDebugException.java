package no.stelvio.common.error.logging;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * LoggingTestDebugException.
 * 
 * @author person08f1a7c6db2c, Accenture
 *
 */
public class LoggingTestDebugException extends SystemUnrecoverableException {

	private static final long serialVersionUID = -7025428216821607999L;

	/**
	 * Creates a new instance of LoggingTestDebugException.
	 *
	 * @param message message
	 */
	public LoggingTestDebugException(String message) {
		super(message);
	}

}
