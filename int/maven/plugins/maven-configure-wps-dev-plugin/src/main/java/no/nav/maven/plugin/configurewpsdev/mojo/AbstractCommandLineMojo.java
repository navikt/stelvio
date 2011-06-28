package no.nav.maven.plugin.configurewpsdev.mojo;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * Abstract class using the template pattern for child mojos.
 * 
 * @author test@example.com
 */
public abstract class AbstractCommandLineMojo extends AbstractMojo {
	
	/**
	 * @parameter expression="${adminPasswd}"
	 */
	protected String adminPasswd;

	protected abstract void doExecute() throws MojoExecutionException, MojoFailureException;

//	protected abstract String getGoalPrettyPrint();

	public void execute() throws MojoExecutionException, MojoFailureException {
		doExecute();
	}

	protected final int executeCommand(Commandline command) {
		final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
		
		
		try {
			// If a password is sent as a parameter, we hide it from the output
//			if (command.toString().contains("-password")) {
//				String cmd = command.toString().replaceFirst("-password\\s[\\w]+", "-password *****");
//				getLog().info("Executing the following command: " + cmd);
//			}
			if (command.toString().contains(adminPasswd)) {
				String cmd = command.toString().replace(adminPasswd, "*****");
				getLog().info(df.format(new Date()) + " Executing the following command: " + cmd);
			}
			else {
				getLog().info(df.format(new Date()) + " Executing the following command: " + command.toString());
			}
			
			

			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().info(df.format(new Date()) + " " + line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					getLog().error(df.format(new Date()) + " " + line);
				}
			};
			ErrorCheckingStreamConsumer errorChecker = new ErrorCheckingStreamConsumer();

			int retval = CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut).add(errorChecker),
					new StreamConsumerChain(systemErr).add(errorChecker));

			if (errorChecker.isError()) {
						throw new RuntimeException("An error occured during setup.");
			}
			
			return retval;
			
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + command, e);
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
			if (line.toLowerCase().contains("error")) {
				error = true;
			}
		}

		public boolean isError() {
			return error;
		}
	}
}
