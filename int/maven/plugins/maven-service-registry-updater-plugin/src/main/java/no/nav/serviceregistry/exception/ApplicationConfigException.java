package no.nav.serviceregistry.exception;

public class ApplicationConfigException extends RuntimeException {
	private static final long serialVersionUID = 2169477382806225400L;

	public ApplicationConfigException() {}

	public ApplicationConfigException(String paramString) {
		super(paramString);
	}

	public ApplicationConfigException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public ApplicationConfigException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}
