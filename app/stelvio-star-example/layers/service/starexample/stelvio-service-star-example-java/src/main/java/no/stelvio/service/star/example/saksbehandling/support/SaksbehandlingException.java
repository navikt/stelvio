package no.stelvio.service.star.example.saksbehandling.support;

import no.stelvio.common.error.FunctionalUnrecoverableException;

/**
 * Base exception for all exceptions in saksbehandling.
 *
 * @author personf8e9850ed756, Accenture
 */
public abstract class SaksbehandlingException extends FunctionalUnrecoverableException {
	public SaksbehandlingException(Object... templateArguments) {
		super(templateArguments);
	}

	public SaksbehandlingException(Throwable cause, Object... templateArguments) {
		super(cause, templateArguments);
	}
}
