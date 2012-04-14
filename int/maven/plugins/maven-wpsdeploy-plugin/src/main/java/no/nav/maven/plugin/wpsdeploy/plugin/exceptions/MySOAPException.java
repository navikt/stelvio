package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class MySOAPException extends RuntimeException {
	private static final long serialVersionUID = 7420416597198479884L;

	public MySOAPException() {
		super();
	}

	public MySOAPException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public MySOAPException(String arg0) {
		super(arg0);
	}

	public MySOAPException(Throwable arg0) {
		super(arg0);
	}
}
