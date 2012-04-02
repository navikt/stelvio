package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class MySOAPException extends RuntimeException {
	private static final long serialVersionUID = 460749273498346737L;
	public MySOAPException() {}
	public MySOAPException(String message) {
		super(message);
	}
}
