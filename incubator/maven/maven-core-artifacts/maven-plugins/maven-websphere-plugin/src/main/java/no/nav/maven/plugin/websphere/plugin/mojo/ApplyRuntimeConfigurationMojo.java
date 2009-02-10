package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.List;
import java.util.Set;

import no.nav.busconfiguration.configuration.ArtifactConfiguration;
import no.nav.busconfiguration.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
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
public class ApplyRuntimeConfigurationMojo extends WebsphereUpdaterMojo  {
    	
	protected final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
	
		for(Artifact a : artifacts) {
			if(a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				ConfigurationType configuration = ArtifactConfiguration.getConfiguration(a.getArtifactId());
				if(configuration != null &&  configuration.getRuntime() != null && configuration.getRuntime().getActivationspecifications()!=null) {
					updateActivationSpecifications(configuration.getRuntime().getActivationspecifications(), commandLine);
				}
			}
		}
	}
	
	private final void updateActivationSpecifications(ActivationspecificationsType activationSpecifications, Commandline commandLine) {
			
		List<ActivationspecificationType> specifications = activationSpecifications.getActivationspecificationList();
			
		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + baseDirectory + "/" + scriptDirectory + "/scripts/ModifyMaxConcurrencyAS.py");
		commandLine.addArg(arg);
		
		for(ActivationspecificationType a : specifications) {
			try {
				Commandline detailedCommand = new Commandline();
				detailedCommand.addArguments(commandLine.getArguments());
				arg = new Commandline.Argument();
				arg.setLine(a.getName());
				detailedCommand.addArg(arg);
				arg = new Commandline.Argument();
				arg.setLine(String.valueOf(a.getValue()));
				detailedCommand.addArg(arg);
				getLog().info("Executing the following command: " + commandLine.toString());
				CommandLineUtils.executeCommandLine(commandLine, stdout, stderr);
				detailedCommand.clearArgs();
				reportResult(stdout, stderr);
			} catch (CommandLineException e) {
				throw new RuntimeException("An error occured executing: " + commandLine, e);
			}
		}
		
	}
}	