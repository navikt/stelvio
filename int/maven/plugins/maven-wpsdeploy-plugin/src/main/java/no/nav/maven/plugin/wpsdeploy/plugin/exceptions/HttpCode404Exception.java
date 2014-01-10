package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class HttpCode404Exception extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public HttpCode404Exception() {
		super();
	}

	public HttpCode404Exception(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public HttpCode404Exception(String arg0) {
		super(arg0);
	}

	public HttpCode404Exception(Throwable arg0) {
		super(arg0);
	}
}
