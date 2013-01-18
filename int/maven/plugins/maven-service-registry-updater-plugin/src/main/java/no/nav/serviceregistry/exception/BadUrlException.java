package no.nav.serviceregistry.exception;

public class BadUrlException extends RuntimeException {
	private static final long serialVersionUID = 2169477382806225400L;

	public BadUrlException() {}

	public BadUrlException(String paramString) {
		super(paramString);
	}

	public BadUrlException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public BadUrlException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}
