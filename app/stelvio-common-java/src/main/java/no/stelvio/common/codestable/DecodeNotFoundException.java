package no.stelvio.common.codestable;

/**
 * CodesTableConfigurationException
 * @author personf8e9850ed756
 * @todo write javadoc
 * @version $Id$
 */
public class DecodeNotFoundException extends CodesTableException {

	/**
	 * 
	 * @param message
	 */
	public DecodeNotFoundException(String message) {
		super(message);
	}

	protected String messageTemplate() {
		return "No decode found for code {0}";
	}
}