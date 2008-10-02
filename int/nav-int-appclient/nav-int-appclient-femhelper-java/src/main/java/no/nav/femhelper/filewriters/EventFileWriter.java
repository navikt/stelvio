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

import no.nav.appclient.util.Constants;

import org.apache.commons.lang.StringUtils;

import utils.SDOFormatter;

import com.ibm.wbiserver.manualrecovery.FailedEventParameter;
import com.ibm.wbiserver.manualrecovery.FailedEventWithParameters;
import com.ibm.websphere.management.AdminClient;
import commonj.sdo.DataObject;


/**
 * This class that writes events to file
 * 
 * @author Andreas Røe
 */

public class EventFileWriter extends AbstractFileWriter {
	
	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(EventFileWriter.class.getName());
	
	/**
	 * Default parameterized constructor
	 * 
	 * @param path path
	 * @param filename filename
	 * @throws IOException 
	 */
	public EventFileWriter(String path, String filename) throws IOException {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "EventFileWriter()");
		
		if (StringUtils.isEmpty(path)) {
			String tempFolderProperty = "java.io.tmpdir";
			String tempFolder = System.getProperty(tempFolderProperty);
			path = tempFolder;
		}
		
		String completePath = path + File.separatorChar + filename;
		LOGGER.log(Level.FINE, "Creating instance of BufferedWriter with path: " + completePath);
		writer = new BufferedWriter(new FileWriter(completePath, true));
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "EventFileWriter()");
	}
	
	/**
	 * write the header in the csv file
	 */
	public void writeHeader () throws IOException {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "writeHeader");
		
		writer.write("MessageId" + separator);
		writer.write("SessionId" + separator);
		writer.write("InteractionType" + separator);
		writer.write("SourceModule" + separator);
		writer.write("SourceComponent" + separator);
		writer.write("DestinationModule" + separator);
		writer.write("DestinationComponent" + separator);
		writer.write("DestinationMethod" + separator);
		writer.write("FailureDate" + separator);
		writer.write("FailureMessage" + separator);
		writer.write("DataObject" + separator);
		writer.write("CorrelationId");
		writer.newLine();
		writer.flush();
		
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "writeHeader");
	}
	
	/**
	 * Format from FEM format to CSV and write
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeCSVEvent (FailedEventWithParameters parameters, AdminClient adminClient, String dateFormat) throws IOException {
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "writeCSVEvent");
		
		// Write information about this event
		writer.write(getEscapedString(parameters.getMsgId()) + separator);
		writer.write(getEscapedString(parameters.getSessionId()) + separator);
		writer.write(getEscapedString(parameters.getInteractionType()) + separator);
		writer.write(getEscapedString(parameters.getSourceModuleName()) + separator);
		writer.write(getEscapedString(parameters.getSourceComponentName()) + separator);
		writer.write(getEscapedString(parameters.getDestinationModuleName()) + separator);
		writer.write(getEscapedString(parameters.getDestinationComponentName()) + separator);
		writer.write(getEscapedString(parameters.getDestinationMethodName()) + separator);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		writer.write(getEscapedString(sdf.format(parameters.getFailureDateTime())) + separator);
		
		// Write quotes start. This is needed to get all values within
	 	// the same cell if and when this data is imported in Excel.
	 	writer.write("\"");
	 	//LS, looks like a bug in the FEM MBEAN because only get 1024 bytes back - message is truncated
		writer.write(getEscapedString(parameters.getFailureMessage())+ "...(truncated to max. 1024 bytes)");
	 	// Write quotes end plus separator
	 	writer.write("\""+ separator);
		
		// Write parameters from the failed event
	 	if (parameters instanceof FailedEventWithParameters) {
	 		
	 		List paramList = parameters.getFailedEventParameters(adminClient.getConnectorProperties());
		 	
		 	// Write quotes start. This is needed to get all values within
		 	// the same cell if and when this data is imported in Excel.
		 	writer.write("\"");
		 	
		 	for (Iterator itBO = paramList.iterator(); itBO.hasNext();) {
		 	    // Each parameter is know as a type of FailedEventParameter.
		 	    FailedEventParameter failedEventParameter = (FailedEventParameter) itBO.next();
		 	    SDOFormatter sdoppt = new SDOFormatter(2, " ");

				LOGGER.log(Level.FINEST, "getName: " + failedEventParameter.getName());
				LOGGER.log(Level.FINEST, "getType: " + failedEventParameter.getType());
				LOGGER.log(Level.FINEST, "getPosition: " + failedEventParameter.getPosition());
				LOGGER.log(Level.FINEST, "getValue: " + failedEventParameter.getValue());
		 	   
		 	    String prettyPrint = null;
		 	    if (failedEventParameter.getValue() instanceof DataObject) {
		 	    	sdoppt.sdoPrettyPrint((DataObject)failedEventParameter.getValue()); 
		 	    } else if (failedEventParameter.getValue() instanceof String) {
		 	    	prettyPrint = (String) failedEventParameter.getValue();
		 	    } else {
		 	    	prettyPrint = "Unable to convert";
		 	    }
		 	    writer.write(getEscapedString("DataObject:" + prettyPrint));
		 	    writer.write(EMPTY); // Ensure all 'cells' are filled to improve make the view even more easy to read
		 	}
		 			 	
		 	// Write quotes end
		 	writer.write("\"" + separator);
	 	}
	 	
	 	// Write PIID / CorrelationId
	 	writer.write(getEscapedString(parameters.getCorrelationId()));

	 	
	 	// Write a empty line the end of this entry and close the writer
	 	writer.newLine();
	 	
	 	writer.flush();
	 	
	 	LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "writeCSVEvent");
	}

	
	/**
	 * write the discard header in the csv file
	 */
	public void writeShortHeader () throws IOException {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "writeShortHeader");
		writer.write("MessageId" + separator);
		writer.write("Status" + separator);
		writer.write("FailureDate" + separator);
		writer.write("FailureMessage");
		writer.newLine();
		writer.flush();
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "writeShortHeader");
	}

	/**
	 * Format from FEM format to CSV and write
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeShortEvent (String MsgId, String Status, String fDate, String fMsg) throws IOException {
		
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "writeShortEvent");
		
		// Write information about this event
		writer.write(getEscapedString(MsgId) + separator);
		writer.write(getEscapedString(Status) + separator);
		writer.write(getEscapedString(fDate) + separator);
		
		// Write quotes start. This is needed to get all values within
	 	// the same cell if and when this data is imported in Excel.
	 	writer.write("\"");

		writer.write(getEscapedString(fMsg));
	 	// Write quotes end plus separator
	 	writer.write("\"");

	 	// Write a empty line the end of this entry and close the writer
	 	writer.newLine();
	 	
	 	writer.flush();
	 	
	 	LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "writeShortEvent");
	}
	
	/**
	 * TODO: might be more complexity is necessary to filter out other chars tha sep.
	 * Returns a string with all <code>;</code> characters
	 * replace it with <code>#</code> 
	 * @param s String to replace
	 * @return fixed string
	 */
	private String getEscapedString(String s) {
		LOGGER.log(Level.FINE, Constants.METHOD_ENTER + "getEscapedString");
		if (s != null) {	
			String result = s.replaceAll(";", "#"); 
			//double tegn we don't need
			result = result.replaceAll("##", "#");
			return result;
		} 
		LOGGER.log(Level.FINE, Constants.METHOD_EXIT + "getEscapedString");
		return StringUtils.EMPTY;
	}
}
