package no.stelvio.presentation.security.sso.accessmanager;
/**
 * Thrown if a representation of userprincipal fails to be converted to a StelvioPrincipal.
 * 
 * @author persondab2f89862d3
 */
public class PrincipalNotValidException extends Exception {
	
	private static final long serialVersionUID = 9176184347217608012L;
	
	/**
	 * Constructor with a message 
	 * @param message the message
	 */
	public PrincipalNotValidException(String message){
		super(message);
	}
	/**
	 * Constructor with a message and a root cause
	 * @param message the message
	 * @param cause the root cause of the exception
	 */
	public PrincipalNotValidException(String message, Throwable cause){
		super(message, cause);
	}
}
