package no.nav.femhelper.filewriters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.femhelper.common.Constants;
import no.nav.femhelper.common.Event;

import org.apache.commons.lang.StringUtils;

import utils.SDOFormatter;

import com.Ostermiller.util.CSVPrinter;
import com.ibm.wbiserver.manualrecovery.FailedEventParameter;
import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.websphere.management.AdminClient;
import commonj.sdo.DataObject;

/**
 * This class that writes events to file
 * 
 * @author Andreas Røe
 */

public class EventFileWriter {
	private static final String EMPTY = " ";

	/**
	 * Delimiter to use in CSV file
	 */
	private static final char DELIMITER = ';';

	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(EventFileWriter.class.getName());

	private CSVPrinter csvPrinter;

	/**
	 * Default parameterized constructor
	 * 
	 * @param path
	 *            path
	 * @param filename
	 *            filename
	 * @throws IOException
	 */
	public EventFileWriter(String path, String filename) throws IOException {
		if (StringUtils.isEmpty(path)) {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty);
			path = tempFolder;
		}

		File file = new File(path, filename);
		LOGGER.log(Level.FINE, "Creating writer with path: " + file.getAbsolutePath());
		this.csvPrinter = new CSVPrinter(new BufferedWriter(new FileWriter(file, true)));
		this.csvPrinter.changeDelimiter(DELIMITER);
	}

	/**
	 * Close the EventWriterFile and flush all
	 */
	public void close() {
		try {
			csvPrinter.close();
			csvPrinter = null;
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "ERROR: Might not all reported due to IOException : StackTrace:");
			e.printStackTrace();
		}
	}

	/**
	 * write the header in the csv file
	 */
	public void writeHeader() throws IOException {
		csvPrinter.write("MessageId");
		csvPrinter.write("SessionId");
		csvPrinter.write("InteractionType");
		csvPrinter.write("SourceModule");
		csvPrinter.write("SourceComponent");
		csvPrinter.write("DestinationModule");
		csvPrinter.write("DestinationComponent");
		csvPrinter.write("DestinationMethod");
		csvPrinter.write("FailureDate");
		csvPrinter.write("FailureMessage");
		csvPrinter.write("DataObject");
		csvPrinter.writeln("CorrelationId");
	}

	/**
	 * Format from FEM format to CSV and write
	 * 
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeCSVEvent(FailedEventWithParameters parameters, Event event, AdminClient adminClient) throws IOException {
		// Write information about this event
		csvPrinter.write(parameters.getMsgId());
		csvPrinter.write(parameters.getSessionId());
		csvPrinter.write(parameters.getInteractionType());
		csvPrinter.write(parameters.getSourceModuleName());
		csvPrinter.write(parameters.getSourceComponentName());
		csvPrinter.write(parameters.getDestinationModuleName());
		csvPrinter.write(parameters.getDestinationComponentName());
		csvPrinter.write(parameters.getDestinationMethodName());
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
		csvPrinter.write(sdf.format(parameters.getFailureDateTime()));

		// LS, looks like a bug in the FEM MBEAN because only get 1024 bytes
		// back - message is truncated
		csvPrinter.write(parameters.getFailureMessage() + "...(truncated to max. 1024 bytes)");

		// Write parameters from the failed event
		StringBuilder sb = new StringBuilder();
		if (parameters instanceof FailedEventWithParameters) {
			try {
				List paramList = parameters.getFailedEventParameters(adminClient.getConnectorProperties());

				for (Iterator itBO = paramList.iterator(); itBO.hasNext();) {
					// Each parameter is know as a type of FailedEventParameter.
					FailedEventParameter failedEventParameter = (FailedEventParameter) itBO.next();
					SDOFormatter sdoppt = new SDOFormatter(2, " ");

					// Probing in data type
					String prettyPrint = null;
					
					// This if statement was before used for different behavior for
					// objects of DataObject and objects of String. Keeping this
					// statement to make this differense in the request element 
					// obvious.
					if (failedEventParameter.getValue() instanceof DataObject) {
						prettyPrint = failedEventParameter.getValue().toString();
					} else if (failedEventParameter.getValue() instanceof String) {
						prettyPrint = failedEventParameter.getValue().toString();
					} else {
						prettyPrint = "Unable to convert";
					}
					sb.append("DataObject:" + prettyPrint);
					sb.append(EMPTY); // Ensure all 'cells' are filled to
					// improve make the view even more easy
					// to read
				}
			} catch (RuntimeException e) {
				String errormsg = "RuntimeException caught during retrieval of business object" 
					+ e.getClass().getName() + "(" + e.getMessage() + ")";
				sb.append(errormsg);
				sb.append(EMPTY);
				LOGGER.log(Level.SEVERE, errormsg);
			}
		}
		csvPrinter.write(sb.toString());

		// Write PIID / CorrelationId
		csvPrinter.writeln(event.getCorrelationID());
	}

	/**
	 * write the discard header in the csv file
	 */
	public void writeShortHeader() throws IOException {
		csvPrinter.write("MessageId");
		csvPrinter.write("Event Status");
		csvPrinter.write("FailureDate");
		csvPrinter.write("FailureMessage");
		csvPrinter.write("CorrelationId");
		csvPrinter.write("Process Status");
		csvPrinter.writeln("FailureMessage");
	}

	/**
	 * Format from FEM format to CSV and write
	 * 
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeShortEvent(Event event) throws IOException {
		// Write information about this event
		csvPrinter.write(event.getMessageID());
		csvPrinter.write(event.getEventStatus());
		csvPrinter.write(event.getEventFailureDate());
		csvPrinter.write(event.getEventFailureMessage());
		csvPrinter.write(event.getCorrelationID());
		csvPrinter.write(event.getProcessStatus());
		csvPrinter.writeln(event.getProcessFailureMessage());
	}
}
