/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * Used for hold the message attributes we need for reporting
 * 
 * @author persona2c5e3b49756 Schnell
 * @see SIBUS JMSMessage
 */
public class MessageInfo {

	private String systemMessageId;
	private String apiMessageId;
	private String correlationId;
	private Integer approximateLength;

	private String apiUserId;
	private String sysUserId;

	private Long currentTimestamp;
	private Long currentMEArrivalTimestamp;
	private Long currentMessageWaitTimestamp;

	private String busName;
	private Integer redeliveredCount;
	private String reliability;

	private String problemDestination;
	private String origDestinationBus;
	private String origDestination;

	private String exceptionMessage;
	private Long exceptionTimestamp;
	private Integer exceptionReason;

	private String messageType;
	private String messageBodyType;

	private byte[] msgContent;
	private StringBuffer msgStringBuffer;

	private String status;

	/**
	 * @param systemMessageId
	 * @param apiMessageId
	 * @param correlationId
	 * @param approximateLength
	 * @param apiUserId
	 * @param sysUserId
	 * @param currentTimestamp
	 * @param currentMEArrivalTimestamp
	 * @param currentMessageWaitTimestamp
	 * @param busName
	 * @param redeliveredCount
	 * @param reliability
	 * @param origDestinationBus
	 * @param origDestination
	 * @param exceptionMessage
	 * @param exceptionTimestamp
	 * @param exceptionReason
	 * @param messageType
	 * @param messageBodyType
	 * @param msgContent
	 */
	public MessageInfo(String systemMessageId, String apiMessageId, String correlationId, int approximateLength,
			String apiUserId, String sysUserId, Long currentTimestamp, Long currentMEArrivalTimestamp,
			Long currentMessageWaitTimestamp, String busName, Integer redeliveredCount, String reliability,
			String problemDestination, String origDestinationBus, String origDestination, String exceptionMessage,
			Long exceptionTimestamp, int exceptionReason, String messageType, String messageBodyType, byte[] msgContent,
			String status) {

		this.systemMessageId = "";
		this.apiMessageId = "";
		this.correlationId = "";
		this.approximateLength = 0;
		this.apiUserId = "";
		this.sysUserId = "";
		this.currentTimestamp = 0L;
		this.currentMEArrivalTimestamp = 0L;
		this.currentMessageWaitTimestamp = 0L;
		this.busName = "";
		this.redeliveredCount = 0;
		this.reliability = "";
		this.problemDestination = "";
		this.origDestinationBus = "";
		this.origDestination = "";
		this.exceptionMessage = "";
		this.exceptionTimestamp = 0L;
		this.exceptionReason = 0;
		this.messageType = "";
		this.messageBodyType = "";
		this.msgContent = null;
		msgStringBuffer = null;
		this.status = null;

		this.systemMessageId = systemMessageId;
		this.apiMessageId = apiMessageId;
		this.correlationId = correlationId;
		this.approximateLength = approximateLength;
		this.apiUserId = apiUserId;
		this.sysUserId = sysUserId;
		this.currentTimestamp = currentTimestamp;
		this.currentMEArrivalTimestamp = currentMEArrivalTimestamp;
		this.currentMessageWaitTimestamp = currentMessageWaitTimestamp;
		this.busName = busName;
		this.redeliveredCount = redeliveredCount;
		this.reliability = reliability;
		this.problemDestination = problemDestination;
		this.origDestinationBus = origDestinationBus;
		this.origDestination = origDestination;
		this.exceptionMessage = exceptionMessage;
		this.exceptionTimestamp = exceptionTimestamp;
		this.exceptionReason = exceptionReason;
		this.messageType = messageType;
		this.messageBodyType = messageBodyType;
		this.msgContent = msgContent;
		this.status = status;
	}

	/**
	 * 
	 */
	public MessageInfo() {
		systemMessageId = "";
		apiMessageId = "";
		correlationId = "";
		approximateLength = 0;
		apiUserId = "";
		sysUserId = "";
		currentTimestamp = 0L;
		currentMEArrivalTimestamp = 0L;
		currentMessageWaitTimestamp = 0L;
		busName = "";
		redeliveredCount = 0;
		reliability = "";
		problemDestination = "";
		origDestinationBus = "";
		origDestination = "";
		exceptionMessage = "";
		exceptionTimestamp = 0L;
		exceptionReason = 0;
		messageType = "";
		messageBodyType = "";
		msgContent = null;
		msgStringBuffer = null;
		status = "";
	}

	/**
	 * @return the apiMessageId
	 */
	public String getApiMessageId() {
		return apiMessageId;
	}

	/**
	 * @param apiMessageId
	 *            the apiMessageId to set
	 */
	public void setApiMessageId(String apiMessageId) {
		this.apiMessageId = apiMessageId;
	}

	/**
	 * @return the apiUserId
	 */
	public String getApiUserId() {
		return apiUserId;
	}

	/**
	 * @param apiUserId
	 *            the apiUserId to set
	 */
	public void setApiUserId(String apiUserId) {
		this.apiUserId = apiUserId;
	}

	/**
	 * @return the approximateLength
	 */
	public Integer getApproximateLength() {

		if (approximateLength == null) {
			approximateLength = 0;
		}

		return approximateLength;
	}

	/**
	 * @param approximateLength
	 *            the approximateLength to set
	 */
	public void setApproximateLength(Integer approximateLength) {
		this.approximateLength = approximateLength;
	}

	/**
	 * @return the busName
	 */
	public String getBusName() {
		return busName;
	}

	/**
	 * @param busName
	 *            the busName to set
	 */
	public void setBusName(String busName) {
		this.busName = busName;
	}

	/**
	 * @return the correlationId
	 */
	public String getCorrelationId() {
		return correlationId;
	}

	/**
	 * @param correlationId
	 *            the correlationId to set
	 */
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	/**
	 * @return the currentMEArrivalTimestamp
	 */
	public Long getCurrentMEArrivalTimestamp() {
		return currentMEArrivalTimestamp;
	}

	/**
	 * @param currentMEArrivalTimestamp
	 *            the currentMEArrivalTimestamp to set
	 */
	public void setCurrentMEArrivalTimestamp(Long currentMEArrivalTimestamp) {
		this.currentMEArrivalTimestamp = currentMEArrivalTimestamp;
	}

	/**
	 * @return the currentMessageWaitTimestamp
	 */
	public Long getCurrentMessageWaitTimestamp() {
		return currentMessageWaitTimestamp;
	}

	/**
	 * @param currentMessageWaitTimestamp
	 *            the currentMessageWaitTimestamp to set
	 */
	public void setCurrentMessageWaitTimestamp(Long currentMessageWaitTimestamp) {
		this.currentMessageWaitTimestamp = currentMessageWaitTimestamp;
	}

	/**
	 * @return the currentTimestamp
	 */
	public Long getCurrentTimestamp() {
		return currentTimestamp;
	}

	/**
	 * @param currentTimestamp
	 *            the currentTimestamp to set
	 */
	public void setCurrentTimestamp(Long currentTimestamp) {
		this.currentTimestamp = currentTimestamp;
	}

	/**
	 * @return the exceptionMessage
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}

	/**
	 * @param exceptionMessage
	 *            the exceptionMessage to set
	 */
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	/**
	 * @return the exceptionReason
	 */
	public Integer getExceptionReason() {

		if (exceptionReason == null) {
			exceptionReason = 0;
		}

		return exceptionReason;
	}

	/**
	 * @param exceptionReason
	 *            the exceptionReason to set
	 */
	public void setExceptionReason(Integer exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	/**
	 * @return the exceptionTimestamp
	 */
	public Long getExceptionTimestamp() {
		return exceptionTimestamp;
	}

	/**
	 * @param exceptionTimestamp
	 *            the exceptionTimestamp to set
	 */
	public void setExceptionTimestamp(Long exceptionTimestamp) {
		this.exceptionTimestamp = exceptionTimestamp;
	}

	/**
	 * @return the messageBodyType
	 */
	public String getMessageBodyType() {
		return messageBodyType;
	}

	/**
	 * @param messageBodyType
	 *            the messageBodyType to set
	 */
	public void setMessageBodyType(String messageBodyType) {
		this.messageBodyType = messageBodyType;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return the msgContent
	 */
	public byte[] getMsgContent() {
		return msgContent;
	}

	/**
	 * @param msgContent
	 *            the msgContent to set
	 */
	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}

	/**
	 * @return the problemDestination
	 */
	public String getProblemDestination() {
		return problemDestination;
	}

	/**
	 * @param problemDestination
	 *            the problemDestination to set
	 */
	public void setProblemDestination(String problemDestination) {
		this.problemDestination = problemDestination;
	}

	/**
	 * @return the origDestination
	 */
	public String getOrigDestination() {
		return origDestination;
	}

	/**
	 * @param origDestination
	 *            the origDestination to set
	 */
	public void setOrigDestination(String origDestination) {
		this.origDestination = origDestination;
	}

	/**
	 * @return the origDestinationBus
	 */
	public String getOrigDestinationBus() {
		return origDestinationBus;
	}

	/**
	 * @param origDestinationBus
	 *            the origDestinationBus to set
	 */
	public void setOrigDestinationBus(String origDestinationBus) {
		this.origDestinationBus = origDestinationBus;
	}

	/**
	 * @return the redeliveredCount
	 */
	public Integer getRedeliveredCount() {

		if (redeliveredCount == null) {
			redeliveredCount = 0;
		}

		return redeliveredCount;
	}

	/**
	 * @param redeliveredCount
	 *            the redeliveredCount to set
	 */
	public void setRedeliveredCount(Integer redeliveredCount) {
		this.redeliveredCount = redeliveredCount;
	}

	/**
	 * @return the reliability
	 */
	public String getReliability() {
		return reliability;
	}

	/**
	 * @param reliability
	 *            the reliability to set
	 */
	public void setReliability(String reliability) {
		this.reliability = reliability;
	}

	/**
	 * @return the systemMessageId
	 */
	public String getSystemMessageId() {
		return systemMessageId;
	}

	/**
	 * @param systemMessageId
	 *            the systemMessageId to set
	 */
	public void setSystemMessageId(String systemMessageId) {
		this.systemMessageId = systemMessageId;
	}

	/**
	 * @return the sysUserId
	 */
	public String getSysUserId() {
		return sysUserId;
	}

	/**
	 * @param sysUserId
	 *            the sysUserId to set
	 */
	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	/**
	 * @return the msgStringBuffer
	 */
	public StringBuffer getMsgStringBuffer() {
		return msgStringBuffer;
	}

	/**
	 * @param msgStringBuffer
	 *            the msgStringBuffer to set
	 */
	public void setMsgStringBuffer(StringBuffer msgStringBuffer) {
		this.msgStringBuffer = msgStringBuffer;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public java.lang.String toString() {
		return "MessageInfo@" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)) + ": " + "{ ID="
				+ systemMessageId + ", busName=" + busName + ", API_ID=" + apiMessageId + ", correlationId=" + correlationId
				+ ", approximateLength=" + approximateLength + ", messageType=" + messageType + ", messageBodyType="
				+ messageBodyType + ", apiUserId=" + apiUserId + ", sysUserId=" + sysUserId + ", redeliveredCount="
				+ redeliveredCount + ", currentTimestamp=" + currentTimestamp + ", currentMEArrivalTimestamp="
				+ currentMEArrivalTimestamp + ", currentMessageWaitTimestamp=" + currentMessageWaitTimestamp + ", reliability="
				+ reliability + ", problemDestination=" + problemDestination + ", origDestinationBus=" + origDestinationBus
				+ ", exceptionMessage=" + exceptionMessage + ", exceptionTimestamp=" + exceptionTimestamp
				+ ", exceptionReason=" + exceptionReason + ", message=" + msgStringBuffer + "}";
	}

}
