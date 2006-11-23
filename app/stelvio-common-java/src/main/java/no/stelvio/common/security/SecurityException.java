package no.stelvio.common.security;

import no.stelvio.common.error.UnrecoverableException;

public abstract class SecurityException extends UnrecoverableException {

	protected SecurityException(Throwable t,Object... templateArguments) {
		super(t,templateArguments);
	}
	protected SecurityException(Object... templateArguments) {
		super(templateArguments);
	}
}
