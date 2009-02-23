package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.io.FilenameFilter;
import java.util.StringTokenizer;

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
 * @goal deploy-resources
 * @requiresDependencyResolution
 */
public class DeployResourcesMojo extends WebsphereUpdaterMojo {
    	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		deployResources(commandLine);
	}
	
	private final void deployResources(final Commandline commandLine) {
	
		String[] applicationsWithProperties = new File(deployableArtifactsHome).list(new FilenameFilter() {public boolean accept(File dir, String name) {return (name.endsWith(".ear")) ? true : false;}});
			
		File resourceProperties = new File(resourcePropertiesHome);
		
		for( String application : applicationsWithProperties) {
			/* TODO: The appname stuff must be sorted out! */
			final String appname = new StringTokenizer(application,".").nextToken();
			String[] matches = resourceProperties.list(new FilenameFilter() {public boolean accept(File dir, String name) {return (name.contains(appname)) ? true : false;}});
			if(matches != null && matches.length > 0) {
				deployResourceForArtifact(commandLine, appname);
			}
		}
	}
	
	private final void deployResourceForArtifact(final Commandline commandLine, final String appname) {
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

		try {
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/CreateApplicationArtifacts.py " + "nav-cons-sak-infot" + " " + environment + " " + scriptsHome);
			commandLine.addArg(arg);
			getLog().info("Executing the following command: " + commandLine.toString());
			CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
			reportResult(stdout, stderr);
			
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		} 
	}
}	