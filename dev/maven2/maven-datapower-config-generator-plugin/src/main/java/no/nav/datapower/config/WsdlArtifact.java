package no.nav.datapower.config;

import org.apache.maven.artifact.Artifact;

public class WsdlArtifact {

	private String groupId;
	private String artifactId;
	private String type = "wsdl-interface";

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean equals(Artifact artifact) {
		if (!artifact.getGroupId().equals(getGroupId())) {
			return false;
		}
		// Use regexp-matching for artifactId
		if (!artifact.getArtifactId().matches(getArtifactId())) {
			return false;
		}
		if (!artifact.getType().equals(getType())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getGroupId() + ":" + getArtifactId() + ":" + getType();
	}

}