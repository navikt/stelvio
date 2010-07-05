package no.nav.maven.plugin.websphere.plugin.mojo;

import no.nav.maven.plugin.websphere.plugin.utils.MojoLauncher;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author test@example.com
 * 
 * Executes the 'generate' goal on the maven-propertiesgenerator-plugin
 * 
 * @goal interpolate-config
 * @requiresDependencyResolution
 */
public class InterpolatorMojo extends WebsphereUpdaterMojo {

	/**
	 * @parameter expression="${envClass}"
	 * @required
	 */
	protected String envClass;

	/**
	 * @parameter expression="${environment}"
	 * @required
	 */
	protected String environment;
	

	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		MojoLauncher.executePropertiesGeneratorMojo(project, session, pluginManager);
	}

	protected String getGoalPrettyPrint() {
		return "Interpolate configuration";
	}
}
