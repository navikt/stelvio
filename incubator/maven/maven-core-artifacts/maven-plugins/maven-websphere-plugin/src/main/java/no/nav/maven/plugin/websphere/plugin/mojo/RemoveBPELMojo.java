package no.nav.maven.plugin.websphere.plugin.mojo;

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
 * @goal remove-bpel
 * @requiresDependencyResolution
 */
public class RemoveBPELMojo extends WebsphereUpdaterMojo {
    	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		for(Artifact a : artifacts) {
			if(a.getArtifactId().contains("ppen010") || a.getArtifactId().contains("hentinstitusjonsoppholdliste")) {
				removeBPEL(commandLine,a.getArtifactId());
			}
		}
	}
	
	private final void removeBPEL(final Commandline commandLine, final String artifactId) {
		
		
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		try {	
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/bpcTemplates.jacl -uninstall " + artifactId + "App");
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