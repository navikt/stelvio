package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that removes the modules containing short-running business processes.
 * Note: The modules which are removed are defined statically inside this class' constructor.
 * 
 * @author test@example.com
 * 
 * @goal remove-bp
 * @requiresDependencyResolution
 */ 

public class RemoveBPMojo extends WebsphereUpdaterMojo {

	private String modulesToRemove;

	/* 
	 * Builds up a command line to send as a argument to the RemoveOldBPModule.py script
	 */
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		
		modulesToRemove = "";
		
		for (Artifact a : artifacts){
			if (a.getArtifactId().contains("-microflow-") 
					|| a.getArtifactId().equals("nav-bsrv-frg-hentinstitusjonsoppholdliste")){
				modulesToRemove += a.getArtifactId() +"="+ a.getVersion() +" ";
			}
		}
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("RemoveOldBPModule.py " + modulesToRemove);
		wsadminCommandLine.addArg(arg);
		
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Remove short-running business processes";
	}
	

}
