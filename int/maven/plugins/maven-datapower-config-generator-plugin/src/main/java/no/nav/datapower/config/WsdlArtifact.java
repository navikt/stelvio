package no.nav.datapower.config;

import org.apache.maven.artifact.Artifact;
import java.lang.Object;

public class WsdlArtifact {

	private String groupId;
	private String artifactId;
	private String type = "wsdl-interface";
	private String classifier = "wsexports";

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
	
	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		if ("NONE".equals(classifier)) {
			this.classifier = null;
		} else {
			this.classifier = classifier;
		}
	}

	public boolean equals(Artifact artifact) {
		return equals(artifact, getType(), getClassifier());
	}

	public boolean equals(Artifact artifact, String type, String classifier) {
		if (!artifact.getGroupId().equals(getGroupId())) {
			return false;
		}
		// Use regexp-matching for artifactId
		if (!artifact.getArtifactId().matches(getArtifactId())) {
			return false;
		}
		if (type != null && !artifact.getType().equals(type)) {
			return false;
		}
		return equals(classifier, artifact.getClassifier());
	}
	
	private static boolean equals(Object a, Object b){
		return (a == b) || (a != null && a.equals(b));
	}

	@Override
	public String toString() {
		return getGroupId() + ":" + getArtifactId() + ":" + getType() + ":" + getClassifier();
	}

}