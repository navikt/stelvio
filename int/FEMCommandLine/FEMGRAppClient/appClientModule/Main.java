import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.actions.AbstractAction;
import no.nav.femhelper.actions.ActionFactory;
import no.nav.femhelper.cmdoptions.CommandOptionsBuilder;
import no.nav.femhelper.common.Constants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import utils.ArgumentValitator;
import utils.PropertyUtil;


/**
 *  
 * @author persona2c5e3b49756 Schnell
 * @author Andreas R�e
 */
public class Main {

	private static Logger LOGGER = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws IOException {

		String sPath = "/tmp"; 
		String sFilename = "default_events.txt";
		
		LOGGER.log(Level.INFO, "FEMGRAppClient for WPS 6.1 - Version 1.0");
		
		CommandOptionsBuilder optionsBuilder = new CommandOptionsBuilder();
		Options options = optionsBuilder.getOptions();
		CommandLine cl = null;
		
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException parseEx) {
			System.out.println("Incorrect arguments (listed below). Due to this will the application now terminate");
			System.out.println(parseEx.getMessage());
			System.exit(-1); // TODO AR Find correct return code for error
		}
		
		if (cl.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.setWidth(500);
			
			System.out.println("============================================================");
			System.out.println("Fail Event Manager (FEM) Helper");
			System.out.println("============================================================");
			System.out.println("Sample usage: launchClient.sh FEMGRAppClientEAR.ear " +
					"--configFile=/was_app/config/fem/fem.properties " +
					"--logFilePath=/was_app/logs/fem --maxResultSet=10 " +
					"--maxResultSetPaging=false --messageType=ALL --action=REPORT");		
			formatter.printHelp("FEM HELPER", options);
			System.exit(0);
		}
		
		if (!cl.hasOption(Constants.configFile))	{	
			LOGGER.log(Level.WARNING, Constants.METHOD_ERROR + "The property file is not spesified");
			System.exit(0);
		} 
		
		String configFilePath = cl.getOptionValue(Constants.configFile);
		File propertyFile = new File(configFilePath);
		if (!propertyFile.exists()) {
			LOGGER.log(Level.WARNING, Constants.METHOD_ERROR + "The property file does not exist");
			System.exit(0);
		}
		
		ArgumentValitator argumentValidator = new ArgumentValitator();
		List validatedArguments = argumentValidator.validate(cl);
		if (!validatedArguments.isEmpty()) {
			// Log all errors that occured in validation
			for (int i = 0; i < validatedArguments.size(); i++) {
				LOGGER.log(Level.WARNING, (String) validatedArguments.get(i));
			}
			
			LOGGER.log(Level.WARNING, "Exiting due insufficient arguments. See details listed above");
			System.exit(0);
		}
		
		// It is more or less okay to throw these exceptions from 
		// this method as we have give the user a log entry about
		// this and simultainisly abort the program before this
		// will occur.
		Properties connectProps = new Properties();
		connectProps.load(new FileInputStream(configFilePath));
		
		// Log warnings from property validation if any violations and exit
		PropertyUtil propertyUtil = new PropertyUtil(); 
		List validatedProperties = propertyUtil.validateProperties(connectProps);
		
		if (!validatedProperties.isEmpty()) {
			for (int i = 0; i < validatedProperties.size(); i++) {
				LOGGER.log(Level.WARNING, (String) validatedProperties.get(i));
			}
			LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to issues with missing or invalid parameter in the configuration file - logged above!");
			System.exit(0);
		}

		try {
			LOGGER.log(Level.FINE, "Get instance of EventClient.");
			AbstractAction statusAction = ActionFactory.getAction(Constants.actionOptions[3], connectProps);
//			if (!statusAction.isConnected()) {
//				LOGGER.log(Level.SEVERE, "FEMGRAppClient will now end due to issues with connecting to Failed Event Manager - logged above!");
//				return;
//			}

			// Get the total number of events to match with the page size to avoid hughe amount of retrieving
			Long numberOfEvents = (Long) statusAction.process(null, null, null, false, 0, 0, cl);
			if (numberOfEvents.intValue() < 1) {
				LOGGER.log(Level.SEVERE, "The is no events on this Fail Event Manager. The application will terminat");
				System.exit(0);
			} else {
				LOGGER.log(Level.INFO, "ACTION REQUIERED! There is events on Fail Event Manager");
			}
			
			// Set to max. fem entries if pagsizse is greater
			int lPagesize = Integer.valueOf(cl.getOptionValue(Constants.maxResultSet));
			if (lPagesize > numberOfEvents)
			{
				lPagesize = numberOfEvents.intValue();
				LOGGER.log(Level.INFO, "Set working result set to total number of events (" + lPagesize + ") because MAX_RESULT_SET > TotalEvents.");
			}
			
			// Determine paging
			boolean bPaging = Boolean.parseBoolean(cl.getOptionValue(Constants.maxResultSetPaging).toLowerCase());
			
			String sEdaType = cl.getOptionValue(Constants.messageType);
			String sEdaTypeAction = cl.getOptionValue(Constants.action);
			LOGGER.log(Level.INFO, "Running scenario " + sEdaTypeAction + " with filter option " + sEdaType + " and paging option is set to " + bPaging);

			sPath = cl.getOptionValue(Constants.logFilePath);
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
			Date currentTime = GregorianCalendar.getInstance().getTime();
			sFilename = Constants.FILE_PREFIX + "_" + sEdaTypeAction + "_" + sEdaType  +"_"+ sdf.format(currentTime) + ".csv";
			
			AbstractAction action = ActionFactory.getAction(sEdaTypeAction, connectProps);
			action.process(sPath, sFilename, sEdaType, bPaging, numberOfEvents, lPagesize, cl);
			
						
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, Constants.METHOD_ERROR + "Exception:StackTrace:");
			e.printStackTrace();
		}
		LOGGER.log(Level.INFO, "FEMGRAppClient done...Thx for using it!");
	}
}