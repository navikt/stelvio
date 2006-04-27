package no.trygdeetaten.common.framework;

/**
 * Common constants for framework.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2736 $, $Date: 2006-01-12 03:24:02 +0100 (Thu, 12 Jan 2006) $
 */
public final class Constants {

	/**
	 * The key into a <code>ServiceRequest</code> which will hold an array of <code>ServiceRequest</code>s when there is
	 * multiple messages to send.
	 */
	public static final String JMSSERVICE_MULTIPLE_MESSAGES = "JMSSERVICE_MULTIPLE_MESSAGES";
	/**
	 * The key into a <code>ServiceRequest</code> which hold the configured correlation id, for example a fnr when calling TPS. 
	 */
	public static final String JMSSERVICE_CORRELATION_ID = "JMSSERVICE_CORRELATION_ID";

	/**
	 * Should not be instantiated.
	 */
	private Constants() {
	}
}
