/**
 * 
 */
package no.nav.sibushelper.helper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author persona2c5e3b49756 Schnell
 * 
 */
public class DestinationInfo {

	private String destinationName;
	private String description;
	private String uuid;
	private String type;
	private String targetName;
	private String targetBus;
	private ArrayList<String> queuePoints;
	private ArrayList<String> mediationPoints;
	private ArrayList<String> publicationPoints;

	/**
	 * @param destinationName
	 * @param type
	 * @param description
	 * @param uuid
	 * @param targetName
	 * @param targetBus
	 */
	public DestinationInfo(String destinationName, String type, String description, String uuid, String targetName,
			String targetBus) {
		this.destinationName = null;
		this.description = "";
		this.uuid = null;
		this.type = null;
		this.targetName = "";
		this.targetBus = "";
		queuePoints = new ArrayList<String>();
		mediationPoints = new ArrayList<String>();
		publicationPoints = new ArrayList<String>();

		this.destinationName = destinationName;
		this.type = type;
		if (description != null) {
			this.description = description;
		}
		this.uuid = uuid;
		if (targetName != null) {
			this.targetName = targetName;
		}
		if (targetBus != null) {
			this.targetBus = targetBus;
		}
	}

	/**
	 * @return
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * @return
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return
	 */
	public String getTargetBus() {
		return targetBus;
	}

	/**
	 * @return
	 */
	public String getTargetName() {
		return targetName;
	}

	/**
	 * @return
	 */
	public boolean isQueue() {
		return type.equals("SIBQueue");
	}

	/**
	 * @return
	 */
	public boolean isTopicSpace() {
		return type.equals("SIBTopicSpace");
	}

	/**
	 * @return
	 */
	public boolean isAlias() {
		return type.equals("SIBDestinationAlias");
	}

	/**
	 * @return
	 */
	public boolean isForeign() {
		return type.equals("SIBDestinationForeign");
	}

	/**
	 * @param queuePointName
	 */
	public void addQueuePoint(String queuePointName) {
		queuePoints.add(queuePointName);
	}

	/**
	 * @param mediationPointName
	 */
	public void addMediationPoint(String mediationPointName) {
		mediationPoints.add(mediationPointName);
	}

	/**
	 * @param publicationPointName
	 */
	public void addPublicationPoint(String publicationPointName) {
		publicationPoints.add(publicationPointName);
	}

	/**
	 * @return
	 */
	public String[] getQueuePoints() {
		String arr[] = new String[queuePoints.size()];
		arr = queuePoints.toArray(arr);
		Arrays.sort(arr);
		return arr;
	}

	/**
	 * @return
	 */
	public String[] getMediationPoints() {
		String arr[] = new String[mediationPoints.size()];
		arr = mediationPoints.toArray(arr);
		Arrays.sort(arr);
		return arr;
	}

	/**
	 * @return
	 */
	public String[] getPublicationPoints() {
		String arr[] = new String[publicationPoints.size()];
		arr = publicationPoints.toArray(arr);
		Arrays.sort(arr);
		return arr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DestinationInfo@" + Integer.toHexString(System.identityHashCode(this)) + ": {" + "destinationName="
				+ destinationName + ", " + "uuid=" + uuid + ", " + "description=" + description + ", " + "queuePoints="
				+ queuePoints + ", " + "mediationPoints=" + mediationPoints + "}";
	}

}
