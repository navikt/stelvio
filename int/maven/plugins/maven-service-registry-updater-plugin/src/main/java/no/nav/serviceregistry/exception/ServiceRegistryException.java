package no.nav.serviceregistry.exception;

public class ServiceRegistryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

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
