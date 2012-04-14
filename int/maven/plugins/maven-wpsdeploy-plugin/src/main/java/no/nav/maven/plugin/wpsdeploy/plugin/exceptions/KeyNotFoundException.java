package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class KeyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -5405525624091367029L;

	public KeyNotFoundException() {
		super();
	}

	public KeyNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public KeyNotFoundException(String arg0) {
		super(arg0);
	}

	public KeyNotFoundException(Throwable arg0) {
		super(arg0);
	}
}
