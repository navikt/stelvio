package no.stelvio.common.codestable;

/**
 * Exception thrown when there are problems configuring the codestable 
 * module.
 * @author personf8e9850ed756
 * @todo write javadoc
 * @version $Id$
 */
public class CodesTableConfigurationException extends CodesTableException {
    
	/**
	 * CodesTableConfigurationException
	 * @param message 
	 */
	public CodesTableConfigurationException(String message) {
        super(message);
    }

    protected String messageTemplate() {
        return "Problems configuration of codes table module: {0}";
    }
}
