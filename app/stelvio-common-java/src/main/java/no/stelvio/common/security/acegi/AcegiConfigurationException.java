package no.stelvio.common.security.acegi;
import org.acegisecurity.AcegiSecurityException;

/**
 * This class represent an exception that occurs when the security configuration is not properly set up.
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AcegiConfigurationException extends AcegiSecurityException{
	
	
	private static final long serialVersionUID = 1L;

	/**
     * Constructs an <code>AcegiConfigurationException</code> with the specified
     * message.
     *
     * @param msg the detail message
     */
    public AcegiConfigurationException(String msg) {
        super(msg);
    }

    /**
     * Constructs an <code>AcegiConfigurationException</code> with the specified
     * message and root cause.
     *
     * @param msg the detail message
     * @param t root cause
     */
    public AcegiConfigurationException(String msg, Throwable t) {
        super(msg, t);
    }


}
