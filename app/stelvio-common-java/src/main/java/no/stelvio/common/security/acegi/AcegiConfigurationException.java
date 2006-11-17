package no.stelvio.common.security.acegi;
import org.acegisecurity.AcegiSecurityException;

/**
 * 
 * @author persondab2f89862d3, Accenture
 * @version $Id$
 */
public class AcegiConfigurationException extends AcegiSecurityException{
	
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
