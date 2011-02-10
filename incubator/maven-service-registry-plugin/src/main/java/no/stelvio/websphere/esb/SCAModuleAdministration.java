package no.stelvio.websphere.esb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;

public class SCAModuleAdministration {

	public static List<Map<String, String>> getJaxWsExports(final Log log, String script, String esbRuntimeRoot, String dmgrHost, int dmgrPort, String username, String password) {
		StreamConsumer systemOut = new StreamConsumer() {
			public void consumeLine(String line) {
				log.debug(line);
			}
		};
		List<Map<String, String>> exports = new ArrayList<Map<String, String>>();
		StreamConsumer valuePairParser = new ParseValuePairStreamConsumer(
				exports);

		Commandline command = new Commandline();
		if (Os.isFamily("windows") == true) {
			command.setExecutable(esbRuntimeRoot + "/bin/wsadmin.bat");
		} else {
			command.setExecutable(esbRuntimeRoot + "/bin/wsadmin.sh");
		}
		Commandline.Argument argScript = new Commandline.Argument();
		argScript
				.setLine("-f " + script);
		command.addArg(argScript);
		Commandline.Argument argTarget = new Commandline.Argument();
		argTarget
				.setLine("-host " + dmgrHost + " -port " + dmgrPort + " -user " + username);
		command.addArg(argTarget);
		log.info("Executing command: " + command + " -password ********");
		Commandline.Argument argPassword = new Commandline.Argument();
		argPassword.setLine("-password " + password);
		command.addArg(argPassword);

		try {
			CommandLineUtils.executeCommandLine(command,
					new StreamConsumerChain(systemOut).add(valuePairParser),
					null);
		} catch (CommandLineException e) {
			throw new RuntimeException(
					"An error occured executing: " + command, e);
		}
		
		return exports;
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

	private static class ParseValuePairStreamConsumer implements StreamConsumer {

		private List<Map<String, String>> valueList;

		public ParseValuePairStreamConsumer(List<Map<String, String>> valueList) {
			this.valueList = valueList;
		}

		public void consumeLine(String line) {
			String[] valuePairStrings = line.split(",");
			Map<String, String> valuePairs = new HashMap<String, String>();
			for (String valuePairString : valuePairStrings) {
				String[] splittedLine = valuePairString.split("=");
				if (splittedLine.length == 2) {
					valuePairs.put(splittedLine[0], splittedLine[1]);
				}
			}
			if (valuePairs.entrySet().size() > 0) {
				valueList.add(valuePairs);
			}
		}
	}

}
