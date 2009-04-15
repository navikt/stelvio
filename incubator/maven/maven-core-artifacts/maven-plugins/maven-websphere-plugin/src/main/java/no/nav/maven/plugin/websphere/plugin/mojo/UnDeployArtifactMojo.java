package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;

import no.nav.maven.commons.constants.Constants;
import no.nav.maven.plugin.websphere.plugin.utils.EarFile;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that contacts local scripts to undeploy all applications from target deployment manager
 * 
 * @author test@example.com
 * 
 * @goal undeploy-artifact
 * @requiresDirectInvocation true
 * @requiresDependencyResolution
 */
public class UnDeployArtifactMojo extends WebsphereUpdaterMojo {
    	
	public final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		
		File destFolder = new File(deployableArtifactsHome);
		if(destFolder.exists() == false) {
			destFolder.mkdir();
		} else {
			getLog().warn("The target folder is not empty. Deleting existing content");
			for( File f : destFolder.listFiles())
				f.delete();
		}
		
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				copyArtifactToTarget(a);
			}
		}
		unDeployArtifacts(commandLine);
	}
	
	private final void unDeployArtifacts(final Commandline commandLine) {
		
		
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		try {
			
			Commandline.Argument arg = new Commandline.Argument();
			arg.setLine("-f " + scriptsHome + "/scripts/UnDeploy.py applications " + environment + " " + scriptsHome + " " + deployableArtifactsHome);
			commandLine.addArg(arg);
			getLog().info("Executing the following command: " + commandLine.toString());
			CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
			reportResult(stdout, stderr);
			
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		} 
	}

	private final void copyArtifactToTarget(Artifact a) {
		File source = new File(a.getFile().getAbsolutePath());		
		File dest = new File(deployableArtifactsHome, a.getArtifactId() + a.getVersion() + "." + a.getType());
		
		EarFile.copyFile(source, dest);
	}

	
	@Override
	protected String getGoalPrettyPrint() {
		return "UnDeploy artifacts from WPS";
	}


}	