package no.nav.serviceregistry.util;

import static no.nav.serviceregistry.util.AppConfigUtils.empty;
import no.nav.aura.appconfig.exposed.Service;
import no.nav.aura.envconfig.client.ApplicationInfo;
import no.nav.serviceregistry.exception.ApplicationConfigException;

public class MvnArtifact {
	private String artifactId, groupId, version, type = "";
	
	public MvnArtifact(String artifactId, String groupId, String version, String type){
		this.setClassProperties(artifactId, groupId, version, type);
	}
	public MvnArtifact(ApplicationInfo applicationInfo, String type){
		this.setClassProperties(applicationInfo.getAppConfigArtifactId(), applicationInfo.getAppConfigGroupId(), applicationInfo.getVersion(), type);
	}
	public MvnArtifact(Service service, String type){
		this.setClassProperties(service.getWsdlArtifactId(), service.getWsdlGroupId(), service.getWsdlVersion(), type);
	}
	
	private void setClassProperties(String artifactId, String groupId, String version, String type){
		this.artifactId = artifactId;
		this.groupId = groupId;
		this.version = version;
		this.type = type;
		if(empty(artifactId) || empty(groupId) || empty(version) || empty(type)){
			throw new ApplicationConfigException("The coordinates needed to create a Maven artifact is missing, check the application configuration for missing/empty values!\n" + this.toString());
		}
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getVersion() {
		return version;
	}

	public String getType() {
		return type;
	}
	
	public String toString(){
		return "ArtifactId: '"+artifactId+"', GroupId: '"+groupId+"', Version: '"+version+"', Type: '"+type+"'";
	}
}
