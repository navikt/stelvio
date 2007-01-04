package no.nav.presentation.pensjon.saksbehandling.stelvio.exceptions;

public class TPSException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private int errorCode;
	
	public TPSException() {
		super();
	}
	
	public TPSException(String message) {
		super(message);
	}
	
	public TPSException(Exception e) {
		super(e);
	}
	
	public TPSException(int errorCode, Exception e) {
		super(e);
		this.errorCode = errorCode;
	}
	
	public TPSException(int errorCode, String exception) {
		super(exception);
		this.errorCode = errorCode;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
}