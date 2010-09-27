package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.MojoLauncher;

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

	protected void applyToWebSphere(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		MojoLauncher.executePropertiesGeneratorMojo(project, session, pluginManager);
	}

	protected String getGoalPrettyPrint() {
		return "Interpolate configuration";
	}
}
