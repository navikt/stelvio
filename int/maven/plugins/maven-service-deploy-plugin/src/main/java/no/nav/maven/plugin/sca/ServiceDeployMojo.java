package no.nav.maven.plugin.sca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * This plugin builds an assembly (zip-file) that can be used as input to
 * serviceDeploy.
 * 
 * @author test@example.com
 * 
 * @goal service-deploy
 * @requiresDependencyResolution
 */
public class ServiceDeployMojo extends AbstractMojo {
	/**
	 * @parameter expression="${wid.runtime}"
	 * @required
	 */
	private String wpsRuntime;

	/**
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	protected File targetDirectory;

	/**
	 * @parameter expression="${project.build.finalName}-sd.zip"
	 * @required
	 * @readonly
	 */
	private String input;

	/**
	 * @parameter expression="${project.build.finalName}.${project.artifact.artifactHandler.extension}"
	 * @required
	 * @readonly
	 */
	private String output;
	
	/**
	 * @parameter expression="${project.artifact}"
	 * @required
	 * @readonly
	 */
	private Artifact artifact;

	/**
	 * @parameter
	 */
	private String classpath;

	/**
	 * @parameter default-value="UTF-8"
	 */
	private String fileEncoding;

	/**
	 * @parameter default-value="false"
	 */
	private boolean freeform;

	/**
	 * @parameter default-value="false"
	 */
	private boolean ignoreErrors;

	/**
	 * @parameter default-value="false"
	 */
	private boolean cleanStagingModules;

	/**
	 * @parameter default-value="false"
	 */
	private boolean keep;

	/**
	 * @parameter default-value="false"
	 */
	private boolean noJ2eeDeploy;

	/**
	 * @parameter default-value="false"
	 */
	private boolean skipXsdValidate;

	/**
	 * @parameter default-value="${project.build.directory}/serviceDeploy"
	 * @required
	 */
	private File workingDirectory;

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		Commandline commandLine;
		if (Os.isFamily("windows")) {
			commandLine = new Commandline(wpsRuntime + "/bin/serviceDeploy.bat");
		} else {
			commandLine = new Commandline(wpsRuntime + "/bin/serviceDeploy.sh");
		}

		StringBuilder argLineBuilder = new StringBuilder();
		argLineBuilder.append(input);
		argLineBuilder.append(" -outputApplication ").append(output);
		argLineBuilder.append(" -progressMonitor none");
		if (classpath != null && classpath.length() > 0) {
			argLineBuilder.append(" -classpath ").append(classpath);
		}
		if (fileEncoding != null && fileEncoding.length() > 0) {
			argLineBuilder.append(" -fileEncoding ").append(fileEncoding);
		}
		if (freeform) {
			argLineBuilder.append(" -freeform");
		}
		if (ignoreErrors) {
			argLineBuilder.append(" -ignoreErrors");
		}
		if (cleanStagingModules) {
			argLineBuilder.append(" -cleanStagingModules");
		}
		if (keep) {
			argLineBuilder.append(" -keep");
		}
		if (noJ2eeDeploy) {
			argLineBuilder.append(" -noJ2eeDeploy");
		}
		if (skipXsdValidate) {
			argLineBuilder.append(" -skipXsdValidate");
		}
		workingDirectory.mkdirs();
		argLineBuilder.append(" -workingDirectory '").append(workingDirectory.getAbsolutePath()).append("'");

		commandLine.setWorkingDirectory(targetDirectory.getAbsolutePath());
		commandLine.createArg().setLine(argLineBuilder.toString());
		executeCommand(commandLine);
		
		artifact.setFile(new File(targetDirectory, output));
	}

	private void executeCommand(Commandline commandLine) throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("Executing the following command: " + commandLine.toString());

			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().info(line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().error(line);
				}
			};
			ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();

			int ret = CommandLineUtils.executeCommandLine(commandLine, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (ret != 0 || errorChecker.isError()) {
				throw new MojoFailureException("Errors executing serviceDeploy. Please consult previous output for details.");

			}
		} catch (CommandLineException e) {
			throw new MojoExecutionException("An error occured executing: " + commandLine, e);
		}
	}

	private static class StreamConsumerChain implements StreamConsumer {
		private final Collection<StreamConsumer> chain = new ArrayList<StreamConsumer>();

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

		public void consumeLine(String line) {
			if (line.contains("Run with -ignoreErrors to override.")) {
				error = true;
			}
		}

		public boolean isError() {
			return error;
		}
	}
}
