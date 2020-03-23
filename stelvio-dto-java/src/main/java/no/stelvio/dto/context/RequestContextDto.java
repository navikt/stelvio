package no.stelvio.dto.context;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data Transfer Object representation of <code>no.stelvio.domain.context.RequestContext</code>.
 * 
 * This object must always be WS-I and Java 1.4 compliant
 * 
 *
 */
public class RequestContextDto implements Serializable {

	private static final long serialVersionUID = -7195349883345306766L;

	private String processId = null; // Cannot get any value (always null)

	private String screenId;

	private String moduleId;

	private String transactionId;

	private String componentId;

	private String userId;

	/**
	 * Default no-arg constructor.
	 * 
	 */
	public RequestContextDto() {
	}

	/**
	 * Returns a String representation of the RequestContextDto object.
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return new ToStringBuilder(this).append("componentId", componentId).append("moduleId", moduleId).append("processId",
				processId).append("screenId", screenId).append("transactionId", transactionId).append("userId", userId)
				.toString();
	}

	/**
	 * The usage of this field is to be decided later. Do not assume this field is available. It may be removed if it's found to
	 * be of no use.
	 * 
	 * @return String TBA
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * The usage of this field is to be decided later. Do not assume this field is available. It may be removed if it's found to
	 * be of no use.
	 * 
	 * @param moduleId
	 *            String TBA
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * Gets screeen id.
	 * 
	 * @return the screen id
	 */
	public String getScreenId() {
		return screenId;
	}

	/**
	 * Sets screen id.
	 * 
	 * @param screenId
	 *            the screen id
	 */
	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	/**
	 * Gets transaction id.
	 * 
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the transaction id.
	 * 
	 * @param transactionId
	 *            the transactio id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Gets the componentId that passed the request context.
	 * 
	 * @return componentId
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * Sets the componentId that passed the request context.
	 * 
	 * @param componentId
	 *            the component id
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * Gets the userId of the user that initiated the request.
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the userId of the user that initiated the request.
	 * 
	 * @param userId
	 *            the user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
