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
	private SIBMQLinkReceiverCurrentStatus receiverChannel=null;
	
	/**
	 * 
	 */
	public MQLinkReceiverChannelInfo(SIBMQLinkReceiverCurrentStatus arg0) {

		if (arg0!=null)
		{
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
		}
		else
		{
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

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBatchSize()
	 */
	public Integer getBatchSize() {
		return this.batchSize;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBuffersReceived()
	 */
	public Long getBuffersReceived() {
		return this.buffersReceived;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBuffersSent()
	 */
	public Long getBuffersSent() {
		return this.buffersSent;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBytesReceived()
	 */
	public Long getBytesReceived() {
		return this.bytesReceived;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getBytesSent()
	 */
	public Long getBytesSent() {
		return this.bytesSent;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getChannelStartTimeMillis()
	 */
	public Long getChannelStartTimeMillis() {
		return this.channelStartTimeMillis;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getCurrentLUWID()
	 */
	public String getCurrentLUWID() {
		return this.currentLUWID;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getCurrentSequenceNumber()
	 */
	public Long getCurrentSequenceNumber() {
		return this.currentSequenceNumber;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getHeartbeatInterval()
	 */
	public Integer getHeartbeatInterval() {
		return this.heartbeatInterval;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getIpAddress()
	 */
	public String getIpAddress() {
		return this.ipAddress;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastLUWID()
	 */
	public String getLastLUWID() {
		return this.lastLUWID;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastMessageSendTimeMillis()
	 */
	public Long getLastMessageSendTimeMillis() {
		return this.lastMessageSendTimeMillis;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLastSequenceNumber()
	 */
	public Long getLastSequenceNumber() {
		return this.lastSequenceNumber;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getLinkNpmSpeed()
	 */
	public String getLinkNpmSpeed() {
		return this.linkNpmSpeed.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getMaxMessageLength()
	 */
	public Integer getMaxMessageLength() {
		return this.maxMessageLength;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getMessagesInCurrentBatch()
	 */
	public Integer getMessagesInCurrentBatch() {
		return this.messagesInCurrentBatch;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getNumberOfBatchesReceived()
	 */
	public Long getNumberOfBatchesReceived() {
		return this.numberOfBatchesRec;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getNumberOfMessagesReceived()
	 */
	public Long getNumberOfMessagesReceived() {
		return this.numberOfMessagesRec;
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getQueueManager()
	 */
	public String getQueueManager() {
		return this.queueManager;
	}
	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getStatus()
	 */
	public String getStatus() {
		return this.status.toString();
	}

	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getInstanceHandle()
	 */
		public long getInstanceHandle() {
			return this.instanceHandle;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.websphere.sib.admin.SIBMQLinkReceiverCurrentStatus#getStopRequested()
	 */
	public Boolean getStopRequested() {
		return this.stopRequested;
	}
	
	 /* (non-Javadoc)
     * @see java.lang.arg0ect#toString()
     */
    public String toString()
    {
        return "MQLinkReceiverChannelInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + receiverChannel + "}";
    }    

}
