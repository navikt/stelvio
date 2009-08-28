package no.nav.maven.plugin.websphere.plugin.mojo;

import java.util.List;

import no.nav.maven.commons.configuration.ArtifactConfiguration;
import no.nav.maven.commons.constants.Constants;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ActivationspecificationsType;
import no.nav.pensjonsprogrammet.wpsconfiguration.ConfigurationType;
import no.nav.pensjonsprogrammet.wpsconfiguration.RuntimeType;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
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
public class ApplyRuntimeConfigurationMojo extends WebsphereUpdaterMojo {

	protected final void applyToWebSphere(final Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		for (Artifact a : artifacts) {
			if (a.getType().equals(Constants.EAR_ARTIFACT_TYPE)) {
				for (ConfigurationType configuration : ArtifactConfiguration.getAllConfigurations(a.getArtifactId())) {
					RuntimeType runtime = configuration.getRuntime();
					if (runtime != null) {
						if (runtime.getActivationspecifications() != null) {
							updateActivationSpecifications(runtime.getActivationspecifications(), commandLine);
						}
						if (runtime.getAutostart() != null) {
							updateAutoStart(a.getArtifactId(), runtime.getAutostart(), commandLine);
						}
					}
				}
			}
		}
	}

	private final void updateActivationSpecifications(ActivationspecificationsType activationSpecifications,
			Commandline commandLine) {
		List<ActivationspecificationType> specifications = activationSpecifications.getActivationspecificationList();

		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/ModifyMaxConcurrencyAS.py");
		commandLine.addArg(arg);

		Commandline detailedCommand = new Commandline();
		for (ActivationspecificationType a : specifications) {
			try {
				detailedCommand.setExecutable(commandLine.getExecutable());
				detailedCommand.addArguments(commandLine.getArguments());
				arg = new Commandline.Argument();
				arg.setLine(a.getName());
				detailedCommand.addArg(arg);
				arg = new Commandline.Argument();
				arg.setLine(String.valueOf(a.getMaxconcurrency()));
				detailedCommand.addArg(arg);
				getLog().info("Executing the following command: " + detailedCommand.toString());
				CommandLineUtils.executeCommandLine(detailedCommand, stdout, stderr);
				reportResult(stdout, stderr);
				detailedCommand.clearArgs();
			} catch (CommandLineException e) {
				throw new RuntimeException("An error occured executing: " + commandLine, e);
			}
		}
	}

	private final void updateAutoStart(final String artifactId, final String autoStart, final Commandline commandLine) {

		final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
		final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();

		Commandline.Argument arg = new Commandline.Argument();
		arg.setLine("-f " + scriptsHome + "/scripts/AutoStart.py");
		commandLine.addArg(arg);

		Commandline detailedCommand = new Commandline();
		try {
			detailedCommand.setExecutable(commandLine.getExecutable());
			detailedCommand.addArguments(commandLine.getArguments());
			arg = new Commandline.Argument();
			arg.setLine(artifactId);
			detailedCommand.addArg(arg);
			arg = new Commandline.Argument();
			arg.setLine(autoStart);
			detailedCommand.addArg(arg);
			getLog().info("Executing the following command: " + detailedCommand.toString());
			CommandLineUtils.executeCommandLine(detailedCommand, stdout, stderr);
			reportResult(stdout, stderr);
			detailedCommand.clearArgs();
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + commandLine, e);
		}
	}

	@Override
	protected String getGoalPrettyPrint() {
		return "Apply WPS runtime configuration";
	}
}