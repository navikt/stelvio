package no.stelvio.common.security;

import no.stelvio.common.error.SystemUnrecoverableException;

public abstract class SecurityException extends SystemUnrecoverableException {

	protected SecurityException(Throwable t,Object... templateArguments) {
		super(t,templateArguments);
	}
	protected SecurityException(Object... templateArguments) {
		super(templateArguments);
	}
}
