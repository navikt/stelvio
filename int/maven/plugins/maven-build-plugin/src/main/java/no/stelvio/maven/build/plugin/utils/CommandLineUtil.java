package no.stelvio.maven.build.plugin.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class CommandLineUtil {
	
	//private static boolean fail = false;
	
	public static final int executeCommand(Commandline command) throws MojoFailureException {
		try {
			
			int retval = 0;

			// If a password is sent as a parameter, we hide it from the output
			if (command.toString().contains("-password")) {
				String cmd = command.toString().replaceFirst("-password\\s[\\w]+", "-password *****");
				System.out.println("[INFO] Executing the following command: " + cmd);
				
			} else {
				System.out.println("[INFO] Executing the following command: " + command.toString());
			}
			System.out.println("[INFO] Working directory: " + command.getWorkingDirectory().toString());
			StreamConsumer systemOut = new StreamConsumer() {
				public void consumeLine(String line) {
					System.out.println("[INFO] " + line);
				}
			};
			StreamConsumer systemErr = new StreamConsumer() {
				public void consumeLine(String line) {
					System.err.println("[ERROR] " + line);
				}
			};

			retval = CommandLineUtils.executeCommandLine(command, new StreamConsumerChain(systemOut), new StreamConsumerChain(systemErr));				
			System.out.println("[INFO] [RETVAL = " + retval + "]");
			if (retval != 0) throw new MojoFailureException("Something went wrong. retval="+retval);
			return retval;
		} catch (CommandLineException e) {
			throw new RuntimeException("An error occured executing: " + command, e);
		}
	}

}



class StreamConsumerChain implements StreamConsumer {
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
