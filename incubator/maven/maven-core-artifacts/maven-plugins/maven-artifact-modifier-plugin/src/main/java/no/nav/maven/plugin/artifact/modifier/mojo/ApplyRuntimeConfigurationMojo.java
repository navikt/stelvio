package no.nav.maven.plugin.artifact.modifier.mojo;

import java.util.List;

import no.nav.maven.plugin.artifact.modifier.configuration.ArtifactConfiguration;
import no.nav.maven.plugin.artifact.modifier.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;


/**
 * Goal that contact the deployment manager to inject runtime settings
 * 
 * @author test@example.com
 * 
 * @goal apply-runtime-configuration
 * @requiresDependencyResolution
 */
public class ApplyRuntimeConfigurationMojo extends ArtifactModifierMojo {
    	
	/**
	 * @parameter expression="${wid.home}"
	 * @required
	 */
	protected String widHome;
	
	/**
	 * @parameter expression="${username}"
	 * @required
	 */
	protected String deploymentManagerUser;
	
	/**
	 * @parameter expression="${password}"
	 * @required
	 */
	protected String deploymentManagerPassword;

	/**
	 * @parameter expression="${host}"
	 * @required
	 */
	protected String deploymentManagerHost;
	
	/**
	 * @parameter expression="${port}"
	 * @required
	 */
	protected String deploymentManagerPort;

	
	protected final void doExecute() throws MojoExecutionException, MojoFailureException {
		
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				ConfigurationType configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
				if(configuration != null &&  configuration.getRuntime() != null && configuration.getRuntime().getActivationspecifications()!=null) {
					updateActivationSpecifications(configuration.getRuntime().getActivationspecifications());
				}
			}
		}
	}
	
	private final void updateActivationSpecifications(ActivationspecificationsType activationSpecifications) {
		
		Commandline commandLine = new Commandline();
		if(Os.isFamily("windows") == true) {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.bat");
		} else {
			commandLine.setExecutable(widHome + "/pf/wps01/bin/wsadmin.sh");
		}	

		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
		
		List<ActivationspecificationType> specifications = activationSpecifications.getActivationspecificationList();
	
		
		for(ActivationspecificationType a : specifications) {
			try {
				Commandline.Argument arg1 = new Commandline.Argument();
				arg1.setLine("-host " + deploymentManagerHost);
				commandLine.addArg(arg1);
				Commandline.Argument arg2 = new Commandline.Argument();
				arg2.setLine("-port " + deploymentManagerPort);
				commandLine.addArg(arg2);
				Commandline.Argument arg3 = new Commandline.Argument();
				arg3.setLine("-user " + deploymentManagerUser);
				commandLine.addArg(arg3);
				Commandline.Argument arg4 = new Commandline.Argument();
				arg4.setLine("-password " + deploymentManagerPassword);
				commandLine.addArg(arg4);
				Commandline.Argument arg5 = new Commandline.Argument();
				arg5.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/ModifyMaxConcurrenct.py");
				commandLine.addArg(arg5);
				Commandline.Argument arg6 = new Commandline.Argument();
				arg6.setLine(a.getName());
				commandLine.addArg(arg6);
				Commandline.Argument arg7 = new Commandline.Argument();
				arg7.setLine(String.valueOf(a.getValue()));
				commandLine.addArg(arg7);
				CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
				commandLine.clearArgs();
			} catch (CommandLineException e) {
				throw new RuntimeException("An error occured executing: " + commandLine, e);
			}
		}
		
	}
}	