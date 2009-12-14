package no.nav.datapower.config;

public class Policy {
	
	private WsdlArtifact[] artifacts;

	private String name;
	
	public WsdlArtifact[] getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(WsdlArtifact[] artifacts) {
		this.artifacts = artifacts;
	}

	public String getName() {
		return name;
	}

	public void setName(String policyName) {
		this.name = policyName;
	}

}
