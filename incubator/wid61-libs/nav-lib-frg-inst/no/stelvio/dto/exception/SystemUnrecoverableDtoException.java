package no.stelvio.dto.exception;

public abstract class SystemUnrecoverableDtoException extends UnrecoverableDtoException {

	public SystemUnrecoverableDtoException(Object[] arguments) {
		super(arguments);
	}

	public SystemUnrecoverableDtoException(Throwable cause, Object[] arguments) {
		super(cause, arguments);
	}

}
