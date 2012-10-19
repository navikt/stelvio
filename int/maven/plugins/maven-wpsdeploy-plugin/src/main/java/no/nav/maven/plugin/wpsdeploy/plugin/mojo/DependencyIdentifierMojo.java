package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Goal that resolves which modules to install and deploy resources for
 * 
 * 
 * @goal dependency-identifier
 * @requiresDependencyResolution
 */
public class DependencyIdentifierMojo extends WebsphereUpdaterMojo {

	public final void applyToWebSphere(final Commandline wsadminCommandLine) throws MojoExecutionException, MojoFailureException {
		configurationRequierdToProceed();
		
		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("DependencyIdentifier.py" + " deployAllResources=" + "True");
		wsadminCommandLine.addArg(arg);
		executeCommand(wsadminCommandLine);
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Resolve which modules to install and deploy resources for?";
	}
}