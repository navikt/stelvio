package no.nav.femhelper.common;

import java.io.Serializable;

/**
 * This class encapsulate a FailedEvent's message and correlation id. The class is initially introduces as a work around for the
 * error situation where <code>FailedEventWithParameters</code> objects don't have the event's correlation id between it self
 * and the process instance.
 * 
 * The class is minimized to have the two attributes only to not cause memory issues.
 * 
 * @author Andreas Roe
 */
public class Event implements Serializable {

	/** Serialization UID */
	private static final long serialVersionUID = 3692294873318542392L;

	/** Shall reflect FailedEvent.msgId */
	private String messageID;

	/** Shall reflect FailedEvent.correlationId */
	private String correlationID;

	/** Used in actions to keep state of this object */
	private String eventStatus;

	/** Used in acions to keep state of a process related to this object */
	private String processStatus;

	/** Used in actions to keep the time of failure for this object */
	private String eventFailureDate;

	/** Uses in actions to keep the failure message for this object */
	private String eventFailureMessage;

	/** Used in actions to keep the failure message from termination of connected processes */
	private String processFailureMessage;

	/** Used to denote the failed event type **/
	private String type;

	/** Used to denote the failed event type **/
	private String deploymentTarget;

	/**
	 * Used to indicate deployment target Needed for getting details about BPC Events
	 **/
	private String destinationModuleName;

	public Event(String messageID) {
		super();
		this.messageID = messageID;
	}

	public Event(String messageID, String correlationID) {
		super();
		this.messageID = messageID;
		this.correlationID = correlationID;
	}

	public Event(String messageID, String correlationID, String type) {
		super();
		this.messageID = messageID;
		this.correlationID = correlationID;
		this.type = type;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public String getMessageID() {
		return messageID;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public String getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(String processStatus) {
		this.processStatus = processStatus;
	}

	public String getEventFailureDate() {
		return eventFailureDate;
	}

	public void setEventFailureDate(String eventFailureDate) {
		this.eventFailureDate = eventFailureDate;
	}

	public String getEventFailureMessage() {
		return eventFailureMessage;
	}

	public void setEventFailureMessage(String eventFailureMessage) {
		this.eventFailureMessage = eventFailureMessage;
	}

	public String getProcessFailureMessage() {
		return processFailureMessage;
	}

	public void setProcessFailureMessage(String processFailureMessage) {
		this.processFailureMessage = processFailureMessage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDestinationModuleName() {
		return destinationModuleName;
	}

	public void setDestinationModuleName(String destinationModuleName) {
		this.destinationModuleName = destinationModuleName;
	}

	public String getDeploymentTarget() {
		return deploymentTarget;
	}

	public void setDeploymentTarget(String deploymentTarget) {
		this.deploymentTarget = deploymentTarget;
	}

	/**
	 * Returns this event's message id
	 * 
	 * @return this event's message id
	 */
	public String toString() {
		return this.messageID;
	}

	@Override
	public boolean equals(Object o) {
		return messageID.equals(o);
	}

	@Override
	public int hashCode() {
		return messageID.hashCode();
	}

}
