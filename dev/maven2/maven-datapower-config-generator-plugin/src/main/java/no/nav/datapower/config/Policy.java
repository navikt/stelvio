package no.nav.datapower.config;

public class Policy {
	
	private WsdlArtifact[] artifacts;

	private String name;
	
	private boolean rewriteEndpoint = false;
	
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

	public boolean isRewriteEndpoint() {
		return rewriteEndpoint;
	}

	public void setRewriteEndpoint(boolean rewriteEndpoint) {
		this.rewriteEndpoint = rewriteEndpoint;
	}

}
