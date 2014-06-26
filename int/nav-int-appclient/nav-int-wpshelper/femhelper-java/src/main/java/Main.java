import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.appclient.util.PasswordEncodeDelegate;
import no.nav.femhelper.actions.AbstractAction;
import no.nav.femhelper.actions.ActionFactory;
import no.nav.femhelper.actions.StatusAction;
import no.nav.femhelper.cmdoptions.CommandOptions;
import no.nav.femhelper.cmdoptions.CommandOptionsBuilder;
import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;

import utils.ArgumentUtil;
import utils.ArgumentValidator;
import utils.PropertyUtil;

/**
 * 
 * @author persona2c5e3b49756 Schnell
 * @author Andreas Røe
 */
public class Main {
	private static Logger logger = Logger.getLogger(Main.class.getName());

	private static final int screenWidth = 500;

	public static void main(String[] args) throws IOException {

		System.out.println(getSeparatorLine(100));
		System.out.println("FEM Helper for BPM");
		System.out.println(getSeparatorLine(100));

		CommandOptionsBuilder optionsBuilder = new CommandOptionsBuilder();
		Options options = optionsBuilder.getOptions();
		CommandLine cl = null;

		try {
			cl = new PosixParser().parse(options, args);

		} catch (ParseException parseEx) {
			System.err.println("Incorrect arguments (listed below) - application will terminate.");
			System.err.println(parseEx.getMessage());
			System.exit(-1); // TODO AR Find correct return code for error
		}

		if (cl.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(screenWidth);

			System.out.println("Usage:");
			System.out.println("launchClient <FEMHelper application> -CCpropfile=<configFile> [-CC<name>=<value>] [app args]");
			System.out
					.println("where -CC<name>=<value> are the client container (launchClient) name-value pair arguments app args are application client arguments.");
			System.out.println(StringUtils.EMPTY);
			System.out.println("Sample usage:");
			System.out
					.println("launchClient.sh <FEMHelper application> --configFile=host.properties --action=REPORT");
			System.out.println(StringUtils.EMPTY);
			System.out.println("All filter by attributes might in certain circumstances be empty. "
					+ "If this occurs you might make use of the --sessionIdWildCard parameter");

			formatter.printHelp("FEM HELPER", options);

			System.exit(0);
		}

		if (!cl.hasOption(CommandOptions.configFile)) {
			logger.log(Level.WARNING, Constants.METHOD_ERROR + "Property file not spesified.");
			System.exit(0);
		}

		String propertyFileName = cl.getOptionValue(CommandOptions.configFile);
		File propertyFile = new File(propertyFileName);
		if (!propertyFile.exists()) {
			logger.log(Level.WARNING, Constants.METHOD_ERROR + "The property file does not exist ("
					+ cl.getOptionValue(CommandOptions.configFile) + ")");
			System.exit(0);
		}

		ArgumentValidator argumentValidator = new ArgumentValidator();
		List validatedArguments = argumentValidator.validate(cl);
		if (!validatedArguments.isEmpty()) {
			for (Iterator argumentIter = validatedArguments.iterator(); argumentIter.hasNext();) {
				logger.log(Level.WARNING, (String) argumentIter.next());
			}
			logger.log(Level.WARNING, "Exiting due to insufficient number of arguments. See details listed above");
			System.exit(0);
		}

		// Do XOR encryption of the password in present in the propfile
		PasswordEncodeDelegate encoder = new PasswordEncodeDelegate();
		encoder.encodePassword(propertyFile);

		// It is more or less okay to throw these exceptions from
		// this method as we have give the user a log entry about
		// this and simultainisly abort the program before this
		// will occur.
		FileInputStream in = new FileInputStream(propertyFile);
		Properties connectProps = new Properties();
		connectProps.load(in);
		in.close();

		// Log warnings from property validation if any violations and exit
		PropertyUtil propertyUtil = new PropertyUtil();
		List validatedProperties = propertyUtil.validateProperties(connectProps);

		if (!validatedProperties.isEmpty()) {
			for (Iterator propertyIter = validatedProperties.iterator(); propertyIter.hasNext();) {
				logger.log(Level.WARNING, (String) propertyIter.next());
			}
			logger.log(Level.SEVERE,
					"FEM Helper terminating due to missing or invalid parameters in configuration file");
			System.exit(0);
		}

		try {
			logger.log(Level.FINE, "Create status action instance");
			AbstractAction statusAction = ActionFactory.getAction(Constants.ACTION_STATUS, connectProps);

			// Get the total number of events to match with the page size to
			// avoid huge amount of retrieving
			Long numberOfEvents = (Long) statusAction.process(null, null, null, false, 0, 0, cl);
			System.out.println("Failed Event Manager contains " + numberOfEvents + " events");
			if (numberOfEvents.intValue() < 1) {
				logger.log(Level.INFO, "There are no events on Failed Event Manager. The application will terminate");
				System.exit(0);
			} else {
				logger.log(Level.INFO, "ACTION REQUIRED! There are events on Failed Event Manager");
			}

			Map<String, String> arguments = ArgumentUtil.getArguments(cl);

			// Set to max. fem entries if pagesize is greater
			int pagesize = Integer.parseInt(arguments.get(CommandOptions.maxResultSet));
			if (pagesize > numberOfEvents) {
				pagesize = numberOfEvents.intValue();
				logger.log(Level.INFO, "Set working result set to total number of events (" + pagesize
						+ ") because MAX_RESULT_SET > TotalEvents.");
			}

			// Determine paging
			boolean paging = Boolean.parseBoolean(arguments.get(CommandOptions.maxResultSetPaging));

			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			Date currentTime = GregorianCalendar.getInstance().getTime();
			String edaTypeAction = cl.getOptionValue(CommandOptions.action);
			String filename = Constants.FILE_PREFIX + "_" + edaTypeAction + "_" + sdf.format(currentTime);
			String path = arguments.get(CommandOptions.reportDirectory);

			AbstractAction action = ActionFactory.getAction(edaTypeAction, connectProps);
			Object result = action.process(path, filename, arguments, paging, numberOfEvents, pagesize, cl);
			logger.log(Level.INFO, "FEM Helper finished.");
			
			try {
				// The action's process method is some kind of number 
				// wrapper class, and the application will exit with this code.
				// E.g. for the STATUS action this will be the current 
				// total number of events on FEM.
				int returnCode = 0;
				if (null != result) {
					Integer.parseInt(result.toString());
				}
				
				// Set return code to 255 is the number is higher.
				// This 255 code represent exit code out of range
				if (returnCode > 255) {
					returnCode = 255;
				}
				
				// Spesific exit code for each action
				if (action instanceof StatusAction && returnCode > 0) {
					returnCode = 8; // Error
				}
				
				System.exit(returnCode);
			} catch (NumberFormatException nfe) {
				// The action's process method did not return 
				// a number kind of result. Doing exit with default ('0') value
				System.exit(0);
			} 
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
			e.printStackTrace();
		}
		
		
	}

	private static String getSeparatorLine(int width) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			sb.append("=");
		}
		return sb.toString();
	}
}