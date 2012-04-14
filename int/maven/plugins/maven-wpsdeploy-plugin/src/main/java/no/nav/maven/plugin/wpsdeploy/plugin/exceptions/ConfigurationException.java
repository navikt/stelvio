package no.nav.maven.plugin.wpsdeploy.plugin.exceptions;

public class ConfigurationException extends RuntimeException {
	private static final long serialVersionUID = -273378588473666146L;

	public ConfigurationException() {
		super();
	}

	public ConfigurationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public ConfigurationException(String arg0) {
		super(arg0);
	}

	public ConfigurationException(Throwable arg0) {
		super(arg0);
	}
}
