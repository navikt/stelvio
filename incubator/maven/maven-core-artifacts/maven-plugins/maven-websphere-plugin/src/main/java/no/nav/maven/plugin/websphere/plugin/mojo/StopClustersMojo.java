package no.nav.maven.plugin.websphere.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that contacts the deployment manager to stop MECluster,SupportCluster and WPSCluster.
 * 
 * @author test@example.com
 * 
 * @goal stop-clusters
 * @requiresDependencyResolution
 */
public class StopClustersMojo extends WebsphereUpdaterMojo {
    	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		stopClusters(commandLine);
	}
	
	private final void stopClusters(final Commandline commandLine) {
				
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		try {
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/BUSProcessController.py stop false");
			commandLine.addArg(arg);
			getLog().info("Executing the following command: " + commandLine.toString());
			CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
			reportResult(stdout, stderr);
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		}
	}
}	