import no.nav.bpchelper.actions.Action;
import no.nav.bpchelper.actions.ActionFactory;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Main {
    public static void main(String[] args) {
	new Main().run(args);
    }

    private void run(String[] args) {
	OptionsBuilder optionsBuilder = new OptionsBuilder();
	Options options = optionsBuilder.getOptions();

	CommandLine cl = null;
	try {
	    cl = new PosixParser().parse(options, args);
	} catch (ParseException parseEx) {
	    System.out
		    .println("Incorrect arguments (listed below). Due to this will the application now terminate");
	    System.out.println(parseEx.getMessage());
	    System.exit(-1); // TODO AR Find correct return code for error
	}

	if (cl.hasOption(OptionOpts.HELP)) {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("BPCHelper",
		    "Sample usage: launchClient BPCHelper", options, null);
	    System.exit(0);
	}

	Action action = ActionFactory.getAction(cl);
	action.process();
    }
}