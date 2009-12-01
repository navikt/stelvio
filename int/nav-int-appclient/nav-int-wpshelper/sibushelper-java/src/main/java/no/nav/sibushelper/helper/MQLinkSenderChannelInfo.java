/**
 * 
 */
package no.nav.sibushelper.helper;

import com.ibm.websphere.sib.admin.SIBMQLinkNPMSpeed;
import com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus;
import com.ibm.websphere.sib.admin.SIBMQLinkState;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class MQLinkSenderChannelInfo {

	private Integer batchSize;
	private Long buffersReceived;
	private Long buffersSent;
	private Long bytesReceived;
	private Long bytesSent;
	private Long channelStartTimeMillis;
	private String currentLUWID;
	private Long currentSequenceNumber;
	private Integer heartbeatInterval;
	private Boolean inDoubt;
	private String ipAddress;
	private String lastLUWID;
	private Long lastMessageSendTimeMillis;
	private Long lastSequenceNumber;
	private SIBMQLinkNPMSpeed linkNpmSpeed;
	private Integer maxMessageLength;
	private Integer messagesInCurrentBatch;
	private Long numberOfBatchesSent;
	private Long numberOfMessagesSent;
	private String queueManager;
	private Integer remainingLongRetryStarts;
	private Integer remainingShortRetryStarts;
	private SIBMQLinkState status;
	private Boolean stopRequested;
	private SIBMQLinkSenderCurrentStatus senderChannel = null;

	/**
	 * 
	 */
	public MQLinkSenderChannelInfo(SIBMQLinkSenderCurrentStatus arg0) {

		if (arg0 != null) {
			batchSize = arg0.getBatchSize();
			buffersReceived = arg0.getBuffersReceived();
			buffersSent = arg0.getBuffersSent();
			bytesReceived = arg0.getBytesReceived();
			bytesSent = arg0.getBytesSent();
			channelStartTimeMillis = arg0.getChannelStartTimeMillis();
			currentLUWID = arg0.getCurrentLUWID();
			currentSequenceNumber = arg0.getCurrentSequenceNumber();
			heartbeatInterval = arg0.getHeartbeatInterval();
			inDoubt = arg0.getInDoubt();
			ipAddress = arg0.getIpAddress();
			lastLUWID = arg0.getLastLUWID();
			lastMessageSendTimeMillis = arg0.getLastMessageSendTimeMillis();
			lastSequenceNumber = arg0.getLastSequenceNumber();
			linkNpmSpeed = arg0.getLinkNpmSpeed();
			maxMessageLength = arg0.getMaxMessageLength();
			messagesInCurrentBatch = arg0.getMessagesInCurrentBatch();
			numberOfBatchesSent = arg0.getNumberOfBatchesSent();
			numberOfMessagesSent = arg0.getNumberOfMessagesSent();
			queueManager = arg0.getQueueManager();
			remainingLongRetryStarts = arg0.getRemainingLongRetryStarts();
			remainingShortRetryStarts = arg0.getRemainingShortRetryStarts();
			status = arg0.getState();
			stopRequested = arg0.getStopRequested();
			senderChannel = arg0;
		} else {
			batchSize = 0;
			buffersReceived = 0L;
			buffersSent = 0L;
			bytesReceived = 0L;
			bytesSent = 0L;
			channelStartTimeMillis = 0L;
			currentLUWID = "";
			currentSequenceNumber = 0L;
			heartbeatInterval = 0;
			inDoubt = false;
			ipAddress = "";
			lastLUWID = "";
			lastMessageSendTimeMillis = 0L;
			lastSequenceNumber = 0L;
			linkNpmSpeed = null;
			maxMessageLength = 0;
			messagesInCurrentBatch = 0;
			numberOfBatchesSent = 0L;
			numberOfMessagesSent = 0L;
			queueManager = "SIBUSHELPER";
			remainingLongRetryStarts = 0;
			remainingShortRetryStarts = 0;
			status = null;
			stopRequested = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getBatchSize()
	 */
	public Integer getBatchSize() {
		return batchSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getBuffersReceived()
	 */
	public Long getBuffersReceived() {
		return buffersReceived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getBuffersSent()
	 */
	public Long getBuffersSent() {
		return buffersSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getBytesReceived()
	 */
	public Long getBytesReceived() {
		return bytesReceived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getBytesSent()
	 */
	public Long getBytesSent() {
		return bytesSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getChannelStartTimeMillis()
	 */
	public Long getChannelStartTimeMillis() {
		return channelStartTimeMillis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getCurrentLUWID()
	 */
	public String getCurrentLUWID() {
		return currentLUWID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getCurrentSequenceNumber()
	 */
	public Long getCurrentSequenceNumber() {
		return currentSequenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getHeartbeatInterval()
	 */
	public Integer getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getInDoubt()
	 */
	public Boolean getInDoubt() {
		return inDoubt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getIpAddress()
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getLastLUWID()
	 */
	public String getLastLUWID() {
		return lastLUWID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getLastMessageSendTimeMillis()
	 */
	public Long getLastMessageSendTimeMillis() {
		return lastMessageSendTimeMillis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getLastSequenceNumber()
	 */
	public Long getLastSequenceNumber() {
		return lastSequenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getLinkNpmSpeed()
	 */
	public String getLinkNpmSpeed() {
		return linkNpmSpeed.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getMaxMessageLength()
	 */
	public Integer getMaxMessageLength() {
		return maxMessageLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getMessagesInCurrentBatch()
	 */
	public Integer getMessagesInCurrentBatch() {
		return messagesInCurrentBatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getNumberOfBatchesSent()
	 */
	public Long getNumberOfBatchesSent() {
		return numberOfBatchesSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getNumberOfMessagesSent()
	 */
	public Long getNumberOfMessagesSent() {
		return numberOfMessagesSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getQueueManager()
	 */
	public String getQueueManager() {
		return queueManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getRemainingLongRetryStarts()
	 */
	public Integer getRemainingLongRetryStarts() {
		return remainingLongRetryStarts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getRemainingShortRetryStarts()
	 */
	public Integer getRemainingShortRetryStarts() {
		return remainingShortRetryStarts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getStatus()
	 */
	public String getStatus() {
		return status.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkSenderCurrentStatus#getStopRequested()
	 */
	public Boolean getStopRequested() {
		return stopRequested;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.arg0ect#toString()
	 */
	@Override
	public String toString() {
		return "MQLinkSenderChannelInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + senderChannel + "}";
	}

}
