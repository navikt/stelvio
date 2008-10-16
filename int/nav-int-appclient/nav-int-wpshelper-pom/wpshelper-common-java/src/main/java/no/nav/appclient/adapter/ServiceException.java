package no.nav.appclient.adapter;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ServiceException extends NestableRuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}
