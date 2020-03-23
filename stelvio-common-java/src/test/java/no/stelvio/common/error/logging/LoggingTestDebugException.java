package no.stelvio.common.error.logging;

import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * LoggingTestDebugException.
 * 
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
