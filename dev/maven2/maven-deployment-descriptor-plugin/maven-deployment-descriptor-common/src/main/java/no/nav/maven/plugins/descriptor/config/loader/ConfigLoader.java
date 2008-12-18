package no.nav.maven.plugins.descriptor.config.loader;

import no.nav.maven.plugins.descriptor.config.DeploymentDescriptorMojoConfig;

public interface ConfigLoader<T> {
	
	public DeploymentDescriptorMojoConfig getDeploymentDescriptorMojoConfig();
	
	public T getConfigContext();
	
	public T createConfigContext(String fileName);
}
