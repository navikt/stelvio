package no.stelvio.common.transferobject;

import java.io.Serializable;
import java.util.Locale;

/**
 * Defines a container for context information. The context container can be
 * used to provide context information that is passed to and from the business
 * layer. It can typically be used for logging purposes, but also to implement
 * specific business logic that is dependent on contextual information.
 * 
 * @author personff564022aedd
 * @version $Id$
 */
public interface ContextContainer extends Serializable {

	/**
	 * Gets user id.
	 * 
	 * @return User id.
	 */
	String getUserId();

	/**
	 * Sets user id.
	 * 
	 * @param userId
	 *            User id
	 */
	void setUserId(String userId);

	/**
	 * Sets screen id.
	 * 
	 * @return Screen id.
	 */
	String getScreenId();

	/**
	 * Sets screen id
	 * 
	 * @param screenId
	 *            Screen id.
	 */
	void setScreenId(String screenId);

	/**
	 * Gets module id.
	 * 
	 * @return Module id.
	 */
	String getModuleId();

	/**
	 * Sets module id.
	 * 
	 * @param moduleId
	 *            Module id.
	 */
	void setModuleId(String moduleId);

	/**
	 * Gets process id.
	 * 
	 * @return Process id.
	 */
	String getProcessId();

	/**
	 * Sets proces id.
	 * 
	 * @param processId
	 *            Process id.
	 */
	void setProcessId(String processId);

	/**
	 * Gets transaction id.
	 * 
	 * @return Transaction id.
	 */
	String getTransactionId();

	/**
	 * Sets transaction id.
	 * 
	 * @param transactionId
	 *            Transaction Id.
	 */
	void setTransactionId(String transactionId);

	/**
	 * Gets locale to be used.
	 * 
	 * @return Locale
	 */
	Locale getLocale();

	/**
	 * Gets locale to be used.
	 * 
	 * @param locale
	 *            Locale
	 */
	void setLocale(Locale locale);
}
