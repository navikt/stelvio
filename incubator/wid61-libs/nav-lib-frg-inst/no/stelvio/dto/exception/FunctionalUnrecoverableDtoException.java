package no.stelvio.dto.exception;

public abstract class FunctionalUnrecoverableDtoException extends
		UnrecoverableDtoException {


	private static final long serialVersionUID = 5580718311491877618L;

	public FunctionalUnrecoverableDtoException(Object[] arguments) {
		super(arguments);
	}

	public FunctionalUnrecoverableDtoException(Throwable cause, Object[] arguments) {
		super(cause, arguments);
	}

}
