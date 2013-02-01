package no.nav.serviceregistry.exception;

public class ApplicationNotInEnvConfigException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ApplicationNotInEnvConfigException() {}

	public ApplicationNotInEnvConfigException(String paramString) {
		super(paramString);
	}

	public ApplicationNotInEnvConfigException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public ApplicationNotInEnvConfigException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}
