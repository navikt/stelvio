import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import no.nav.appclient.util.Constants;
import no.nav.appclient.util.PasswordEncodeDelegate;
import no.nav.bpchelper.actions.Action;
import no.nav.bpchelper.actions.ActionFactory;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;
import no.nav.bpchelper.cmdoptions.OptionsValidator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Main {
	private static final int WIDTH = 200;
	private static final String CMD_LINE_SYNTAX = "launchClient <BPCHelper application> [-CC<name>=<value>] [app args]";
	private static final String HEADER;
	private static final Options OPTIONS = new OptionsBuilder().getOptions();

	static {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("where");
		pw.print("    ");
		pw.println("-CC<name>=<value> are the client container (launchClient) name-value pair arguments");
		pw.println("app args are application client arguments.");
		pw
				.println("sample usage: launchClient.sh bpchelper.ear -CCpropfile=host_config.properties -cf host_config.properties -a STATUS");
		HEADER = sw.toString();
	}

	public static void main(String[] args) {
		int returnCode = new Main().run(args);
		System.exit(returnCode);
	}

	private int run(String[] args) {
		CommandLine commandLine;
		try {
			commandLine = new PosixParser().parse(OPTIONS, args);
		} catch (ParseException e) {
			printHelp(e.getMessage());
			return ReturnCodes.ERROR;
		}

		if (commandLine.hasOption(OptionOpts.HELP)) {
			printHelp(null);
			return ReturnCodes.OK;
		}

		Collection<String> validationErrors = OptionsValidator.validate(commandLine);
		if (!validationErrors.isEmpty()) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			for (String validationError : validationErrors) {
				pw.println(validationError);
			}
			printHelp(sw.toString());
			return ReturnCodes.ERROR;
		}

		String configFilename = commandLine.getOptionValue(Constants.configFile);
		File configFile = new File(configFilename);
		new PasswordEncodeDelegate().encodePassword(configFile);

		Action action = ActionFactory.getAction(commandLine);
		return action.execute();
	}

	private void printHelp(String footer) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(WIDTH, CMD_LINE_SYNTAX, HEADER, OPTIONS, footer);
	}
}