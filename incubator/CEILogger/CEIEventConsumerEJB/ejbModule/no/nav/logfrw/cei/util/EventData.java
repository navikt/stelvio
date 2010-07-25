/*
 * Created on Oct 2, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package no.nav.logfrw.cei.util;

/**
 * @author utvikler
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventData {
	
	private boolean error = false;
	private String eventType = null;
	private String payloadType = null;
	private String sourceComponent = null;
	private String sourceInterface = null;
	private String sourceMethod = null;
	private String sourceModule = null;
	private String targetComponent = null;
	private String targetInterface = null;
	private String targetMethod = null;
	private String targetModule = null;
	private String creationTime = null;
	private String localInstanceId = null;
	private String globalInstanceId = null;
	private String wbiSessionId = null;
	private String ecsCurrentId = null;
	private String ecsParrentId = null;
	

	
	/**
	 * @return Returns the eventType.
	 */
	public String getEventType() {
		return eventType;
	}
	/**
	 * @param eventType The eventType to set.
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	/**
	 * @return Returns the payloadType.
	 */
	public String getPayloadType() {
		return payloadType;
	}
	/**
	 * @param payloadType The payloadType to set.
	 */
	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}
	/**
	 * @return Returns the sourceComponent.
	 */
	public String getSourceComponent() {
		return sourceComponent;
	}
	/**
	 * @param sourceComponent The sourceComponent to set.
	 */
	public void setSourceComponent(String sourceComponent) {
		this.sourceComponent = sourceComponent;
	}
	/**
	 * @return Returns the sourceInterface.
	 */
	public String getSourceInterface() {
		return sourceInterface;
	}
	/**
	 * @param sourceInterface The sourceInterface to set.
	 */
	public void setSourceInterface(String sourceInterface) {
		this.sourceInterface = sourceInterface;
	}
	/**
	 * @return Returns the sourceMethod.
	 */
	public String getSourceMethod() {
		return sourceMethod;
	}
	/**
	 * @param sourceMethod The sourceMethod to set.
	 */
	public void setSourceMethod(String sourceMethod) {
		this.sourceMethod = sourceMethod;
	}
	/**
	 * @return Returns the sourceModule.
	 */
	public String getSourceModule() {
		return sourceModule;
	}
	/**
	 * @param sourceModule The sourceModule to set.
	 */
	public void setSourceModule(String sourceModule) {
		this.sourceModule = sourceModule;
	}
	/**
	 * @return Returns the targetComponent.
	 */
	public String getTargetComponent() {
		return targetComponent;
	}
	/**
	 * @param targetComponent The targetComponent to set.
	 */
	public void setTargetComponent(String targetComponent) {
		this.targetComponent = targetComponent;
	}
	/**
	 * @return Returns the targetInterface.
	 */
	public String getTargetInterface() {
		return targetInterface;
	}
	/**
	 * @param targetInterface The targetInterface to set.
	 */
	public void setTargetInterface(String targetInterface) {
		this.targetInterface = targetInterface;
	}
	/**
	 * @return Returns the targetMethod.
	 */
	public String getTargetMethod() {
		return targetMethod;
	}
	/**
	 * @param targetMethod The targetMethod to set.
	 */
	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}
	/**
	 * @return Returns the targetModule.
	 */
	public String getTargetModule() {
		return targetModule;
	}
	/**
	 * @param targetModule The targetModule to set.
	 */
	public void setTargetModule(String targetModule) {
		this.targetModule = targetModule;
	}
	/**
	 * @return Returns the error.
	 */
	public boolean isError() {
		return error;
	}

	/**
	 * @param error The error to set.
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	/**
	 * @param creationTime
	 */
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * @return
	 */
	public String getCreationTime() {
		return creationTime;
	}
	/**
	 * @param localInstanceId
	 */
	public void setLocaleInstanceId(String localInstanceId) {
		this.localInstanceId = localInstanceId;
	}
	/**
	 * @param globalInstanceId
	 */
	public void setGlobaleInstanceId(String globalInstanceId) {
		this.globalInstanceId = globalInstanceId;		
	}
	/**
	 * @return Returns the globalInstanceId.
	 */
	public String getGlobalInstanceId() {
		return globalInstanceId;
	}
	/**
	 * @return Returns the localInstanceId.
	 */
	public String getLocalInstanceId() {
		return localInstanceId;
	}
	/**
	 * @return Returns the ecsCurrentId.
	 */
	public String getEcsCurrentId() {
		return ecsCurrentId;
	}
	/**
	 * @param ecsCurrentId The ecsCurrentId to set.
	 */
	public void setEcsCurrentId(String ecsCurrentId) {
		this.ecsCurrentId = ecsCurrentId;
	}
	/**
	 * @return Returns the ecsParrentId.
	 */
	public String getEcsParrentId() {
		return ecsParrentId;
	}
	/**
	 * @param ecsParrentId The ecsParrentId to set.
	 */
	public void setEcsParrentId(String ecsParrentId) {
		this.ecsParrentId = ecsParrentId;
	}
	/**
	 * @return Returns the wbiSessionId.
	 */
	public String getWbiSessionId() {
		return wbiSessionId;
	}
	/**
	 * @param wbiSessionId The wbiSessionId to set.
	 */
	public void setWbiSessionId(String wbiSessionId) {
		this.wbiSessionId = wbiSessionId;
	}
}
