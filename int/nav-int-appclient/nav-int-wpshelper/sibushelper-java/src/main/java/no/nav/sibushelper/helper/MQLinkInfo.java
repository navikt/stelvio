/**
 * 
 */
package no.nav.sibushelper.helper;

import javax.management.ObjectName;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class MQLinkInfo {

	private MEInfo meInfo;
	private String name;
	private String status;
	private String qmName;
	private ObjectName mBean;
	private ObjectName senderChannelMBean;
	private ObjectName receiverChannelMBean;

	/**
	 * @param meInfo
	 * @param name
	 * @param status
	 * @param qmName
	 * @param mBean
	 */
	public MQLinkInfo(MEInfo meInfo, String name, String status, String qmName, ObjectName mBean) {
		this.meInfo = null;
		this.name = null;
		this.status = null;
		this.qmName = null;
		this.mBean = null;
		senderChannelMBean = null;
		receiverChannelMBean = null;

		this.meInfo = meInfo;
		this.name = name;
		this.qmName = qmName;
		this.status = status;
		this.mBean = mBean;
	}

	/**
	 * @return
	 */
	public MEInfo getMEInfo() {
		return meInfo;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getQueueManagerName() {
		return qmName;
	}

	/**
	 * @return
	 */
	public ObjectName getMBean() {
		return mBean;
	}

	/**
	 * @return
	 */
	public boolean isStarted() {
		return !status.equals("STOPPED");
	}

	/**
	 * @return
	 */
	public ObjectName getReceiverChannelMBean() {
		return receiverChannelMBean;
	}

	/**
	 * @param receiverChannelMBean
	 */
	public void setReceiverChannelMBean(ObjectName receiverChannelMBean) {
		this.receiverChannelMBean = receiverChannelMBean;
	}

	/**
	 * @return
	 */
	public ObjectName getSenderChannelMBean() {
		return senderChannelMBean;
	}

	/**
	 * @param senderChannelMBean
	 */
	public void setSenderChannelMBean(ObjectName senderChannelMBean) {
		this.senderChannelMBean = senderChannelMBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MQLinkInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": " + "{ name=" + name + ", status="
				+ status + ", qmName=" + qmName + ", owningMEName=" + (meInfo == null ? "Unknown" : meInfo.getName())
				+ ", mBean=" + mBean + ", senderMBean=" + senderChannelMBean + ", receiverMBean=" + receiverChannelMBean + "}";
	}

}
