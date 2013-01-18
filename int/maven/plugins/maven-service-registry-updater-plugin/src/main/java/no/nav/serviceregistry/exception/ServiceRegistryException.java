package no.nav.serviceregistry.exception;

public class ServiceRegistryException extends RuntimeException {
	private static final long serialVersionUID = 5071188633564752042L;

	public ServiceRegistryException() {}

	public ServiceRegistryException(String paramString) {
		super(paramString);
	}

	public ServiceRegistryException(Throwable paramThrowable) {
		super(paramThrowable);
	}

	public ServiceRegistryException(String paramString, Throwable paramThrowable) {
		super(paramString, paramThrowable);
	}

}
