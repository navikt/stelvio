/**
 * 
 */
package no.nav.sibushelper.helper;

import javax.management.ObjectName;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class ServerInfo implements Comparable {

	private String serverName;
	private String nodeName;
	private String cellName;
	private String serverPid;
	private String serverVersion;
	private String processType;
	private ObjectName mBean;

	/**
	 * @param serverName
	 * @param serverPid
	 * @param cellName
	 * @param nodeName
	 * @param serverVersion
	 * @param processType
	 * @param mBean
	 */
	public ServerInfo(String serverName, String serverPid, String cellName, String nodeName, String serverVersion,
			String processType, ObjectName mBean) {
		this.serverName = null;
		this.nodeName = null;
		this.cellName = null;
		this.serverPid = null;
		this.serverVersion = null;
		this.processType = null;
		this.mBean = null;
		this.serverName = serverName;
		this.nodeName = nodeName;
		this.cellName = cellName;
		this.serverPid = serverPid;
		this.serverVersion = serverVersion;
		this.processType = processType;
		this.mBean = mBean;
	}

	/**
	 * @return
	 */
	public String getCellName() {
		return cellName;
	}

	/**
	 * @return
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}

	/**
	 * @return
	 */
	public String getServerPid() {
		return serverPid;
	}

	/**
	 * @return
	 */
	public String getServerVersion() {
		return serverVersion;
	}

	/**
	 * @return
	 */
	public javax.management.ObjectName getMBean() {
		return mBean;
	}

	/**
	 * @return
	 */
	public boolean isDeploymentManager() {
		return processType.equals("DeploymentManager");
	}

	/**
	 * @return
	 */
	public boolean isNodeAgent() {
		return processType.equals("NodeAgent");
	}

	/**
	 * @return
	 */
	public boolean isManagedServer() {
		return processType.equals("ManagedProcess");
	}

	/**
	 * @return
	 */
	public boolean isUnManagedServer() {
		return processType.equals("UnManagedProcess");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object other) {
		int result = -1;
		if (other instanceof no.nav.sibushelper.helper.ServerInfo) {
			ServerInfo otherInfo = (no.nav.sibushelper.helper.ServerInfo) other;
			result = serverName.compareTo(otherInfo.serverName);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": " + "{ name=" + serverName + ", node="
				+ nodeName + ", cell=" + cellName + ", serverPid=" + serverPid + ", serverVersion=" + serverVersion
				+ ", processType=" + processType + ", mbean=" + mBean + "}";
	}

}
