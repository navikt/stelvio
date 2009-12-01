/**
 * 
 */
package no.nav.sibushelper.helper;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class BusMemberInfo {

	private String busName;
	private String clusterName;
	private String nodeName;
	private String serverName;

	/**
	 * @param busName
	 * @param clusterName
	 * @param nodeName
	 * @param serverName
	 */
	public BusMemberInfo(String busName, String clusterName, String nodeName, String serverName) {
		this.busName = null;
		this.clusterName = null;
		this.nodeName = null;
		this.serverName = null;
		this.busName = busName;
		this.clusterName = clusterName;
		this.nodeName = nodeName;
		this.serverName = serverName;
	}

	/**
	 * @return
	 */
	public String getBusName() {
		return busName;
	}

	/**
	 * @return
	 */
	public String getClusterName() {
		return clusterName;
	}

	/**
	 * @return
	 */
	public String getName() {
		return clusterName != null ? clusterName : serverName;
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
	public boolean isClustered() {
		return clusterName != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BusMemberInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + "clusterName=" + clusterName
				+ ", " + "nodeName=" + nodeName + ", " + "serverName=" + serverName + "}";
	}

}
