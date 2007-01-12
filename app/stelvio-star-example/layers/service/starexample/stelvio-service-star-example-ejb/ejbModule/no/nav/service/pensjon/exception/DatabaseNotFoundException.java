package no.nav.service.pensjon.exception;

public class DatabaseNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DatabaseNotFoundException() {
		super();
	}
	
	public DatabaseNotFoundException(String message) {
		super(message);
	}
	
	public DatabaseNotFoundException(Exception e) {
		super(e);
	}
	
	public DatabaseNotFoundException(String message, Exception e) {
		super(message, e);
	}
}