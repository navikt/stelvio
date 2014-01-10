package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class HttpCodeNon2XXException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HttpCodeNon2XXException() {
		super();
	}

	public HttpCodeNon2XXException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HttpCodeNon2XXException(String arg0) {
		super(arg0);
	}

	public HttpCodeNon2XXException(Throwable arg0) {
		super(arg0);
	}
}
