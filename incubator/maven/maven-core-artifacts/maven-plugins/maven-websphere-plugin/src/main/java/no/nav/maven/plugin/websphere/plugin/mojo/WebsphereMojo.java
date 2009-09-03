package no.nav.maven.plugin.websphere.plugin.mojo;

import java.io.File;
import java.util.Set;

import no.nav.maven.commons.managers.ArchiveManager;
import no.nav.maven.commons.managers.IArchiveManager;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginManager;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author test@example.com
 */
public abstract class WebsphereMojo extends AbstractMojo {

	/** @component */
	private Prompter prompter;

	/**
	 * Name of module configuration artifact.
	 * 
	 * @parameter default-value="busconfiguration"
	 */
	protected String moduleConfigurationArtifactName;

	/**
	 * The Maven Project Object
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;

	/**
	 * The Maven Session Object
	 * 
	 * @parameter expression="${session}"
	 * @required
	 * @readonly
	 */
	protected MavenSession session;

	/**
	 * The Maven PluginManager Object
	 * 
	 * @component
	 * @required
	 */
	protected PluginManager pluginManager;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.Archiver#jar}"
	 * @required
	 */
	protected Archiver jarArchiver;

	/**
	 * @parameter expression="${component.org.codehaus.plexus.archiver.UnArchiver#jar}"
	 * @required
	 */
	protected UnArchiver jarUnArchiver;

	/**
	 * @parameter expression="${project.basedir}"
	 * @required
	 */
	protected File baseDirectory;

	/**
	 * @parameter expression="${project.build.scriptSourceDirectory}"
	 * @required
	 */
	protected String scriptDirectory;

	/**
	 * @parameter expression="${project.dependencyArtifacts}"
	 * @required
	 */
	protected Set<Artifact> dependencyArtifacts;

	/**
	 * @parameter expression="${interactiveMode}" default-value="false"
	 * @required
	 */
	protected Boolean interactiveMode;

	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

	protected abstract String getGoalPrettyPrint();

	protected IArchiveManager earArchiveManager;
	protected IArchiveManager jarArchiveManager;

	public void execute() throws MojoExecutionException, MojoFailureException {

		if (interactiveMode == true) {
			String answer = null;
			try {
				answer = prompter.prompt("Do you want to perform step \"" + getGoalPrettyPrint() + "\" (y/n)? ", "n");
			} catch (PrompterException e) {
				throw new MojoFailureException(e, "An error occured during prompt input",
						"An error occured during prompt input");
			}
			if ("n".equalsIgnoreCase(answer)) {
				getLog().info("Skipping step: " + getGoalPrettyPrint());
				return;
			}
		}

		jarArchiveManager = new ArchiveManager(jarArchiver, jarUnArchiver);
		doExecute();
	}

	protected final void executeCommand(Commandline command) {
		try {
			getLog().info("Executing the following command: " + command.toString());
			final CommandLineUtils.StringStreamConsumer stdout = new CommandLineUtils.StringStreamConsumer();
			final CommandLineUtils.StringStreamConsumer stderr = new CommandLineUtils.StringStreamConsumer();
			CommandLineUtils.executeCommandLine(command, stdout, stderr);
			reportResult(stdout, stderr);
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + command, e);
		}
	}

	private void reportResult(CommandLineUtils.StringStreamConsumer stdout, CommandLineUtils.StringStreamConsumer stderr) {
		String outPut = null;

		if (stdout != null) {
			outPut = stdout.getOutput();
		}

		if (outPut == null || outPut.trim().length() == 0) {
			if (stderr != null) {
				outPut = stderr.getOutput();
			}
		}

		getLog().info(outPut);

		if (outPut.toLowerCase().contains("error")) {
			if (interactiveMode == true) {
				String answer = null;
				try {
					answer = prompter.prompt("An error occured during step \"" + getGoalPrettyPrint()
							+ "\" . Do you want to abort the deploy process (y/n)? ", "n");
				} catch (PrompterException e) {
					throw new RuntimeException("An error occured during prompt input", e);
				}

				if ("y".equalsIgnoreCase(answer)) {
					throw new RuntimeException("An error occured during deploy. Stopping deployment. Consult the logs.");
				} else {
					return;
				}
			} else {
				throw new RuntimeException("An error occured during deploy. Stopping deployment. Consult the logs.");
			}

		}
	}
}
