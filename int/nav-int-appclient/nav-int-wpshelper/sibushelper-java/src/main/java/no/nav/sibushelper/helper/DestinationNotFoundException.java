/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * @author wpsadmin
 *
 */
public class DestinationNotFoundException extends Exception {

	private static final long serialVersionUID = 0xf03fc15058fef0bbL;

	/**
	 * @param message
	 */
	public DestinationNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DestinationNotFoundException(Throwable cause) {
		super(cause);
	}

}
