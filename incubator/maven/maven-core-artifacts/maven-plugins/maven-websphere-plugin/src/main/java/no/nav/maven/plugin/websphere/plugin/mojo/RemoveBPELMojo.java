package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that makes sure BPROC's are uninstalled if they are part of the deploy.
 * 
 * @author test@example.com
 * 
 * 
 * @goal remove-bpel
 * @requiresDependencyResolution
 */
public class RemoveBPELMojo extends WebsphereUpdaterMojo {
	/**
	 * @parameter expression="${project.properties}"
	 * @required
	 */
	protected Properties properties;	
	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		for(Artifact a : artifacts) {
			if(properties.containsKey(a.getArtifactId())) {
				String property = properties.getProperty(a.getArtifactId());
				if(("bpel").equals(property) == true) {
					removeBPEL(commandLine,a.getArtifactId());
				}
			}
		}
	}
	
	private final void removeBPEL(final Commandline commandLine, final String artifactId) {
		
		
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		getLog().info("Stopping and uninstalling: " + artifactId);
		try {	
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + scriptsHome + "/scripts/bpcTemplates.jacl -uninstall " + artifactId);
			commandLine.addArg(arg);
			getLog().info("Executing the following command: " + commandLine.toString());
			CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
			reportResult(stdout, stderr);
			
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		} 
	}
	
	@Override
	protected String getGoalPrettyPrint() {
		return "Remove BPEL";
	}
}	