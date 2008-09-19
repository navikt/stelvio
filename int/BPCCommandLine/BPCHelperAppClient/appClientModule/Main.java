import no.nav.bpchelper.actions.Action;
import no.nav.bpchelper.actions.ActionFactory;
import no.nav.bpchelper.cmdoptions.OptionOpts;
import no.nav.bpchelper.cmdoptions.OptionsBuilder;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
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
	    logger.error("Error parsing command line. The application now terminate", parseEx);
	    System.exit(-1); // TODO AR Find correct return code for error
	}

	if (cl.hasOption(OptionOpts.HELP)) {
	    HelpFormatter formatter = new HelpFormatter();
	    formatter.printHelp("BPCHelper", "Sample usage: launchClient BPCHelper", options, null);
	    System.exit(0);
	}

	Action action = ActionFactory.getAction(cl);
	action.process();
    }
}