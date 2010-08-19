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

import com.Ostermiller.util.CSVPrinter;
import com.ibm.wbiserver.manualrecovery.FailedEventParameter;
import com.ibm.wbiserver.manualrecovery.SCAEvent;
import com.ibm.websphere.management.AdminClient;
import commonj.sdo.DataObject;
import commonj.sdo.Property;
import commonj.sdo.Type;

/**
 * This class that writes events to file
 * 
 * @author Andreas Røe
 */

public class EventFileWriter {
	private static final String EMPTY = " ";

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
	 * @param delimiter
	 *            delimiter
	 * @throws IOException
	 */
	public EventFileWriter(String path, String filename, char delimiter) throws IOException {
		if (StringUtils.isEmpty(path)) {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty);
			path = tempFolder;
		}

		File file = new File(path, filename);
		LOGGER.log(Level.FINE, "Creating writer with path: " + file.getAbsolutePath());
		this.csvPrinter = new CSVPrinter(new BufferedWriter(new FileWriter(file, true)));
		this.csvPrinter.changeDelimiter(delimiter);
	}

	/**
	 * Close the EventWriterFile and flush all
	 */
	public void close() {
		try {
			csvPrinter.close();
			csvPrinter = null;
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error closing CSV Printer", e);
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
	 * @param scaEvent
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeCSVEvent(SCAEvent scaEvent, Event event, AdminClient adminClient) throws IOException {
		// Write information about this event
		csvPrinter.write(scaEvent.getMsgId());
		csvPrinter.write(scaEvent.getSessionId());
		csvPrinter.write(scaEvent.getInteractionType());
		csvPrinter.write(scaEvent.getSourceModuleName());
		csvPrinter.write(scaEvent.getSourceComponentName());
		csvPrinter.write(scaEvent.getDestinationModuleName());
		csvPrinter.write(scaEvent.getDestinationComponentName());
		csvPrinter.write(scaEvent.getDestinationMethodName());
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
		csvPrinter.write(sdf.format(scaEvent.getFailureDateTime()));

		// LS, looks like a bug in the FEM MBEAN because only get 1024 bytes
		// back - message is truncated
		csvPrinter.write(scaEvent.getFailureMessage() + "...(truncated to max. 1024 bytes)");

		// Write parameters from the failed event
		StringBuilder sb = new StringBuilder();
		try {
			List paramList = scaEvent.getFailedEventParameters(adminClient.getConnectorProperties());

			for (Iterator itBO = paramList.iterator(); itBO.hasNext();) {
				// Each parameter is know as a type of FailedEventParameter.
				FailedEventParameter failedEventParameter = (FailedEventParameter) itBO.next();

				if (failedEventParameter.getValue() instanceof DataObject) {
					toString((DataObject) failedEventParameter.getValue(), sb);
				} else if (failedEventParameter.getValue() instanceof String) {
					sb.append(failedEventParameter.getValue().toString());
				} else {
					sb.append("Unable to convert");
				}
				// Ensure all 'cells' are filled to improve make the view even more easy to read
				sb.append(EMPTY);
			}
		} catch (RuntimeException e) {
			String errormsg = "RuntimeException caught during retrieval of business object" + e.getClass().getName() + "("
					+ e.getMessage() + ")";
			sb.append(errormsg);
			sb.append(EMPTY);
			LOGGER.log(Level.SEVERE, errormsg, e);
		}
		csvPrinter.write(sb.toString());

		// Write PIID / CorrelationId
		csvPrinter.writeln(event.getCorrelationID());
	}

	/**
	 * Append string representation of a DataObject to StringBuilder
	 * 
	 * The method is intented for debugging and logging Namespace is omitted for types and properties
	 * 
	 * @param dataObject
	 *            The dataObject to append as a string
	 * @param resultString
	 *            An initialized StringBuilder to append to
	 */
	private static void toString(DataObject dataObject, StringBuilder resultString) {
		Type type = dataObject.getType();
		resultString.append(type.getName());

		boolean firstIteration = true;
		for (Iterator iter = type.getProperties().iterator(); iter.hasNext();) {
			Property property = (Property) iter.next();
			String name = property.getName();
			Object child = dataObject.get(name);
			if (child != null) {
				if (firstIteration) {
					resultString.append(" (");
					firstIteration = false;
				} else {
					resultString.append(", ");
				}
				if (child instanceof DataObject) {
					toString((DataObject) child, resultString);
				} else if (child instanceof List) {
					resultString.append(name);
					resultString.append(": list[");
					resultString.append(((List) child).size());
					resultString.append("]");
				} else {
					resultString.append(name);
					resultString.append("=");
					resultString.append(child);
				}
			}
		}
		if (!firstIteration) {
			resultString.append(")");
		}
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
