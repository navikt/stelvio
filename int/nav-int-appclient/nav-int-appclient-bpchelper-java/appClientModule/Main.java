import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

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

	private static final String CMD_LINE_SYNTAX = "launchClient <BPCHelper application> [args]";

	private static final Options OPTIONS = new OptionsBuilder().getOptions();

	public static void main(String[] args) {
		Collection<String> argsCollection = new ArrayList<String>(Arrays.asList(args));
		for (Iterator<String> it = argsCollection.iterator(); it.hasNext();) {
			if (it.next().startsWith("-CC")) {
				it.remove();
			}
		}

		int returnCode = new Main().run(argsCollection.toArray(new String[argsCollection.size()]));
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
			for (String validationError: validationErrors) {
				pw.println(validationError);
			}
			printHelp(sw.toString());
			return ReturnCodes.ERROR;
		}

		Action action = ActionFactory.getAction(commandLine);
		return action.execute();
	}

	private void printHelp(String footer) {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(WIDTH, CMD_LINE_SYNTAX, null, OPTIONS, footer);
	}
}