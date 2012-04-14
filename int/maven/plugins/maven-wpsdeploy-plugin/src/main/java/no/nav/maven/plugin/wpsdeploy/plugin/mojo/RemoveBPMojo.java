package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that removes the modules containing short-running business processes.
 * Note: The modules which are removed are defined statically inside this class' constructor.
 * 
 * @goal remove-bp
 * @requiresDependencyResolution
 */ 

public class RemoveBPMojo extends WebsphereUpdaterMojo {
	@Override
	protected void applyToWebSphere(Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("RemoveOldBPModule.py");
		wsadminCommandLine.addArg(arg);
		
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Remove short-running business processes";
	}
	

}
