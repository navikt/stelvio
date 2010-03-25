package no.nav.maven.plugins;

public class WsdlArtifact {
	private String groupId;
	private String artifactId;
	private String version;
	private String type = "wsdl-interface";
	// TODO: This classifier should at some point be changed to the new value (wsexports), but that will break backwards
	// compatability. BEWARE!
	private String classifier = "wsdlif";

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getClassifier() {
		return classifier;
	}

	public void setClassifier(String classifier) {
		this.classifier = classifier;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
