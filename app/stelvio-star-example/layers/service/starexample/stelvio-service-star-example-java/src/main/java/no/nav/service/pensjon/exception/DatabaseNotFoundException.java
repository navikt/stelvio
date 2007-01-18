package no.nav.service.pensjon.exception;

import no.stelvio.common.error.SystemUnrecoverableException;

public class DatabaseNotFoundException extends SystemUnrecoverableException {
	private static final long serialVersionUID = 1L;

	protected DatabaseNotFoundException(String message) {
		super(message);
	}

	protected DatabaseNotFoundException(Throwable cause, String message) {
		super(cause, message);
	}

	protected String messageTemplate(int numArgs) {
		return "DB is not available: {0}";
	}
}