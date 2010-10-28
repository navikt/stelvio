/**
 * 
 */
package no.nav.maven.plugin.bounce.plugin.utils;

/**
 * @author test@example.com
 *
 */
public class InvalideNodeValueException extends Exception {

	/**
	 * 
	 */
	public InvalideNodeValueException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalideNodeValueException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalideNodeValueException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalideNodeValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage(){
		return "Node parsing failed. Invalid or empty value found.\n"+this.getCause();
	}

}
