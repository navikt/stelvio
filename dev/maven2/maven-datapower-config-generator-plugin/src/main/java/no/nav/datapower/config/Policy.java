package no.nav.datapower.config;

public class Policy {
	
	private WsdlArtifact[] artifacts;

	private String name;
	
	private boolean rewriteEndpoints = false;
	
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

	public boolean isRewriteEndpoints() {
		return rewriteEndpoints;
	}

	public void setRewriteEndpoints(boolean rewriteEndpoint) {
		this.rewriteEndpoints = rewriteEndpoint;
	}

}
