package no.nav.maven.plugin.wpsdeploy.plugin.models;

public class DeployArtifact {
	
	private String groupId;
	private String artifactId;
	private String version;
	private String variableName;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	} 	
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	
	@Override
	public String toString() {
		return getGroupId() + ":" + getArtifactId() + ":" + getVersion();
	}
}