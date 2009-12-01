/**
 * 
 */
package no.nav.sibushelper.helper;

import com.ibm.websphere.sib.admin.SIBMQLinkNPMSpeed;
import com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus;
import com.ibm.websphere.sib.admin.SIBMQLinkState;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class MQLinkReceiverChannelInfo {

	private Integer batchSize;
	private Long buffersReceived;
	private Long buffersSent;
	private Long bytesReceived;
	private Long bytesSent;
	private Long channelStartTimeMillis;
	private String currentLUWID;
	private Long currentSequenceNumber;
	private Integer heartbeatInterval;
	private String ipAddress;
	private String lastLUWID;
	private Long lastMessageSendTimeMillis;
	private Long lastSequenceNumber;
	private SIBMQLinkNPMSpeed linkNpmSpeed;
	private Integer maxMessageLength;
	private Integer messagesInCurrentBatch;
	private Long numberOfBatchesRec;
	private Long numberOfMessagesRec;
	private String queueManager;
	private Long instanceHandle;
	private SIBMQLinkState status;
	private Boolean stopRequested;
	private SIBMQLinkReceiverCurrentStatus receiverChannel = null;

	/**
	 * 
	 */
	public MQLinkReceiverChannelInfo(SIBMQLinkReceiverCurrentStatus arg0) {

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
			ipAddress = arg0.getIpAddress();
			lastLUWID = arg0.getLastLUWID();
			lastSequenceNumber = arg0.getLastSequenceNumber();
			linkNpmSpeed = arg0.getLinkNpmSpeed();
			maxMessageLength = arg0.getMaxMessageLength();
			messagesInCurrentBatch = arg0.getMessagesInCurrentBatch();
			numberOfBatchesRec = arg0.getNumberOfBatchesReceived();
			numberOfMessagesRec = arg0.getNumberOfMessagesReceived();
			queueManager = arg0.getQueueManager();
			instanceHandle = arg0.getInstanceHandle();
			status = arg0.getState();
			stopRequested = arg0.getStopRequested();
			receiverChannel = arg0;
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
			ipAddress = "";
			lastLUWID = "";
			lastMessageSendTimeMillis = 0L;
			lastSequenceNumber = 0L;
			linkNpmSpeed = null;
			maxMessageLength = 0;
			messagesInCurrentBatch = 0;
			numberOfBatchesRec = 0L;
			numberOfMessagesRec = 0L;
			queueManager = "SIBUSHELPER";
			instanceHandle = 0L;
			status = null;
			stopRequested = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBatchSize()
	 */
	public Integer getBatchSize() {
		return batchSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBuffersReceived()
	 */
	public Long getBuffersReceived() {
		return buffersReceived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBuffersSent()
	 */
	public Long getBuffersSent() {
		return buffersSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBytesReceived()
	 */
	public Long getBytesReceived() {
		return bytesReceived;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBytesSent()
	 */
	public Long getBytesSent() {
		return bytesSent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getChannelStartTimeMillis()
	 */
	public Long getChannelStartTimeMillis() {
		return channelStartTimeMillis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getCurrentLUWID()
	 */
	public String getCurrentLUWID() {
		return currentLUWID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getCurrentSequenceNumber()
	 */
	public Long getCurrentSequenceNumber() {
		return currentSequenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getHeartbeatInterval()
	 */
	public Integer getHeartbeatInterval() {
		return heartbeatInterval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getIpAddress()
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastLUWID()
	 */
	public String getLastLUWID() {
		return lastLUWID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastMessageSendTimeMillis()
	 */
	public Long getLastMessageSendTimeMillis() {
		return lastMessageSendTimeMillis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastSequenceNumber()
	 */
	public Long getLastSequenceNumber() {
		return lastSequenceNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLinkNpmSpeed()
	 */
	public String getLinkNpmSpeed() {
		return linkNpmSpeed.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getMaxMessageLength()
	 */
	public Integer getMaxMessageLength() {
		return maxMessageLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getMessagesInCurrentBatch()
	 */
	public Integer getMessagesInCurrentBatch() {
		return messagesInCurrentBatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getNumberOfBatchesReceived()
	 */
	public Long getNumberOfBatchesReceived() {
		return numberOfBatchesRec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getNumberOfMessagesReceived()
	 */
	public Long getNumberOfMessagesReceived() {
		return numberOfMessagesRec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getQueueManager()
	 */
	public String getQueueManager() {
		return queueManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getStatus()
	 */
	public String getStatus() {
		return status.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getInstanceHandle()
	 */
	public long getInstanceHandle() {
		return instanceHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getStopRequested()
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
		return "MQLinkReceiverChannelInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + receiverChannel
				+ "}";
	}

}
