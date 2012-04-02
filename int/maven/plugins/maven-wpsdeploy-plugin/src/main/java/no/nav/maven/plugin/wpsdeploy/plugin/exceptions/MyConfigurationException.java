package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class MyConfigurationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public MyConfigurationException() {}
	public MyConfigurationException(String message) {
		super(message);
	}
}
