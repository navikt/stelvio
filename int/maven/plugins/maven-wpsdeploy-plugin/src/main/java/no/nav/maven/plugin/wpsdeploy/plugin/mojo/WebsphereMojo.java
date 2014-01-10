package no.nav.maven.plugin.wpsdeploy.plugin.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import no.nav.maven.plugin.wpsdeploy.plugin.exceptions.MySOAPException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.components.interactivity.Prompter;
import org.codehaus.plexus.components.interactivity.PrompterException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

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
	 * @component roleHint="jar"
	 * @required
	 * @readonly
	 */
	protected Archiver jarArchiver;

	/**
	 * @component roleHint="jar"
	 * @required
	 * @readonly
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
	
	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected String targetDirectory;
	
	/**
	 * @parameter expression="${skip}"
	 */
	private String skipCSV;
	
	
	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

	protected abstract String getGoalPrettyPrint();

	public void execute() throws MojoExecutionException, MojoFailureException {

		if(skipGoalProperty() || skipGoalManualy()){
			getLog().info(String.format("Skipping step: %s (%s)", getGoalPrettyPrint(), getGoalName()));
			return;
		}
		doExecute();
	}
	
	private boolean skipGoalProperty(){		
		if(skipCSV == null)
			return false;
		String[] goalsToSkip = skipCSV.split(",");
		
		String goalName = getGoalName();
		
		for(String skipGoalName : goalsToSkip){
			if(skipGoalName.equals(goalName)){
				return true;
			}
		}
		return false;
	}

	public String getGoalName() { 
		String mojoClassName = getClass().getName();
		PluginDescriptor pluginDescriptor = (PluginDescriptor) getPluginContext().get("pluginDescriptor");
		
		List<MojoDescriptor> mojoDescriptorList = pluginDescriptor.getMojos();
		for (MojoDescriptor mojoDescriptor : mojoDescriptorList) {
			if (mojoDescriptor.getImplementation().equals(mojoClassName)) { 
				return mojoDescriptor.getGoal(); 
			} 
		} 
		throw new Error("Could not find the Maven goal name!");
	} 
	
	private boolean skipGoalManualy() throws MojoFailureException{
		if (interactiveMode == true) {
			String answer = null;
			try {
				answer = prompter.prompt("Do you want to perform step \"" + getGoalPrettyPrint() + "\" (y/n)? ", "n");
			} catch (PrompterException e) {
				throw new MojoFailureException(e, "An error occured during prompt input",
						"An error occured during prompt input");
			}
			if ("n".equalsIgnoreCase(answer)) {
				return true;
			}
		}
		return false;
	}

	protected final void executeCommand(Commandline command) throws MySOAPException {
		try {

			// If a password is sent as a parameter, we hide it from the output
			if (command.toString().contains("-password")) {
				String cmd = command.toString().replaceFirst("-password\\s[\\w]+", "-password *****");
				getLog().info("Executing the following command: " + cmd);
			}
			else {
				getLog().info("Executing the following command: " + command.toString());
			}


			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					System.out.println(line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().error(line);
				}
			};
			ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();

			int retval = CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if( retval == 105){
				throw new MySOAPException("The wsadmin command exited with an error!");
			}

			if (errorChecker.isError() || retval != 0) {
				if (interactiveMode == true) {
					String answer = null;
					try {
						answer = prompter.prompt("An error occurred during step \"" + getGoalPrettyPrint()
								+ "\" . Do you want to abort the deploy process (y/n)? ", "n");
					} catch (PrompterException e) {
						throw new RuntimeException("An error occurred during prompt input", e);
					}

					if ("y".equalsIgnoreCase(answer)) {
						throw new RuntimeException("Stopping deploy because you answered Yes...");
					}
				} else {
					if (errorChecker.isError()){
						throw new RuntimeException(errorChecker.getErrorMessage() + " Wsadmin exit code: " + retval +"!");
					} else {
						throw new RuntimeException("An error occurred during deploy: The wsadmin command exited with a exit code that was not zero (wsadmin exit code: " + retval +")!");
					}
				}
			}

		} catch (CommandLineException e) {
			throw new RuntimeException("An error occurred executing: " + command, e);
		}

	}

	private static class StreamConsumerChain implements StreamConsumer {
		private final Collection<StreamConsumer> chain = new ArrayList<StreamConsumer>();

		@SuppressWarnings("unused")
		public StreamConsumerChain() {
		}

		public StreamConsumerChain(StreamConsumer streamConsumer) {
			add(streamConsumer);
		}

		public StreamConsumerChain add(StreamConsumer streamConsumer) {
			chain.add(streamConsumer);
			return this;
		}

		public void consumeLine(String line) {
			for (StreamConsumer streamConsumer : chain) {
				streamConsumer.consumeLine(line);
			}
		}
	}

	private static class ErrorCheckingStreamConsumer implements StreamConsumer {
		private boolean error;
		private String errorMessage = "An error occurred during deploy: The wsadmin command output had a line that contained the text \"error\"." +
					" Unless the line also contaned \" 0 errors\", then it's ignored!";

		public void consumeLine(String line) {
			/* the " 0 errors" check is added to not get a false error with this message:
			 * CWLIN2003E: The following compilation problems were detected. 0 errors, 87 warnings, 0 informational messages. See the log file for details.
			 */
			if (line.toLowerCase().contains("error")) {
				if ( ! line.toLowerCase().contains(" 0 errors")){
					error = true;
				}
			}
		}

		public boolean isError() {
			return error;
		}

		private String getErrorMessage() {
			return errorMessage;
		}
	}
}
