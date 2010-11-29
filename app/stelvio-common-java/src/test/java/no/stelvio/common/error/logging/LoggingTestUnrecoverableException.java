package no.stelvio.common.error.logging;

import no.stelvio.common.error.ErrorCode;
import no.stelvio.common.error.SystemUnrecoverableException;

/**
 * LoggingTestUnrecoverableException.
 * 
 * @author MA
 *
 */
public class LoggingTestUnrecoverableException extends SystemUnrecoverableException {

	private static final long serialVersionUID = 1254859201046484278L;

	/**
	 * Creates a new instance of LoggingTestUnrecoverableException.
	 *
	 * @param message message
	 * @param errorCode error code
	 */
	public LoggingTestUnrecoverableException(String message, ErrorCode errorCode) {
		super(message, errorCode);
	}

}
