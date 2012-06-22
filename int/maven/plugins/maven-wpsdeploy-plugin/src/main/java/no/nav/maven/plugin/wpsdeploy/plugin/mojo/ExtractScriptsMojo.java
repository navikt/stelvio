package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;

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
public class ExtractScriptsMojo extends WebsphereUpdaterMojo {

	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		Commandline cl = new Commandline();
		cl.setExecutable("unzip");
		
		Commandline.Argument cmdArg = new Commandline.Argument();
		cmdArg.setLine(getJar().getAbsolutePath() + " -d -o '" + targetDirectory + "' scripts/*");
		cl.addArg(cmdArg);
		
		executeCommand(cl);
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
