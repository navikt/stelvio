package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;

import no.nav.maven.plugin.wpsdeploy.plugin.utils.JarExtractor;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that extracts the Jython scripts used by the deploy plugin
 * 
 * @author test@example.com
 * 
 * @goal extract-scripts
 * @requiresDependencyResolution
 */
public class ExtractScripts extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		JarExtractor.extractJar(getJar(), new File(targetDirectory), "scripts/*");
		getLog().info("Successfully extracted the scripts folder into " + targetDirectory + ".");
	}

	private File getJar(){
		return new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Extract scripts";
	}

}
