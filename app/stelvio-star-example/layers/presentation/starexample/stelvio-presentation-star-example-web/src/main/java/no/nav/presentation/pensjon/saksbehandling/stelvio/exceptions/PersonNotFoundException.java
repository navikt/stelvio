package no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions;

public class PersonNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PersonNotFoundException() {
		super();
	}
	
	public PersonNotFoundException(String message) {
		super(message);
	}
	
	public PersonNotFoundException(Exception e) {
		super(e);
	}
	
	public PersonNotFoundException(String message, Exception e) {
		super(message, e);
	}
}