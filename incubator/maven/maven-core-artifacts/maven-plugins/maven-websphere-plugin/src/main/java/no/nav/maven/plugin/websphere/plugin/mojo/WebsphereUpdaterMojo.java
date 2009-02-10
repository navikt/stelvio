package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.Set;

import no.nav.busconfiguration.configuration.ArtifactConfiguration;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public abstract class WebsphereUpdaterMojo extends WebsphereMojo {
    
	/**
	 * @parameter expression="${wid.home}"
	 * @required
	 */
	protected String widHome;
	
	/**
	 * @parameter expression="${username}"
	 * @required
	 */
	protected String deploymentManagerUser;
	
	/**
	 * @parameter expression="${password}"
	 * @required
	 */
	protected String deploymentManagerPassword;

	/**
	 * @parameter expression="${host}"
	 * @required
	 */
	protected String deploymentManagerHost;
	
	/**
	 * @parameter expression="${port}"
	 * @required
	 */
	protected String deploymentManagerPort;

	/**
	 * @parameter expression="${project.artifacts}"
	 * @required
	 */
	protected Set<Artifact> artifacts;	
	
	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 */
	protected File baseDirectory;
	
	/**
	 * @parameter expression="${project.build.scriptSourceDirectory}"
	 * @required
	 */
	protected String scriptDirectory;
	
	protected abstract void applyToWebSphere() throws MojoExecutionException, MojoFailureException;

	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		if(ArtifactConfiguration.isConfigurationLoaded() == false) {
			throw new RuntimeException("The artifact configuration is not loaded");
		}
		
		applyToWebSphere();
	}
}	