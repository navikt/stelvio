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
import com.ibm.wbiserver.manualrecovery.BPCEvent;
import com.ibm.wbiserver.manualrecovery.FailedEvent;
import com.ibm.wbiserver.manualrecovery.FailedEventParameter;
import com.ibm.wbiserver.manualrecovery.JMSEvent;
import com.ibm.wbiserver.manualrecovery.MQEvent;
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
		csvPrinter.write("EventType");
		csvPrinter.write("InteractionType");
		csvPrinter.write("SourceModule");
		csvPrinter.write("SourceComponent");
		csvPrinter.write("DestinationModule");
		csvPrinter.write("DestinationComponent");
		csvPrinter.write("DestinationMethod");
		csvPrinter.write("ResubmitDestination");
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
	public void writeCSVEvent(FailedEvent failedEvent, Event event, AdminClient adminClient) throws IOException {

		LOGGER.log(Level.FINE, "ENTERING writeCSVEvent");
		// Write information about this event
		if (failedEvent != null) {
			csvPrinter.write(failedEvent.getMsgId());
			csvPrinter.write(failedEvent.getSessionId());
			csvPrinter.write(failedEvent.getType());
			csvPrinter.write(failedEvent.getInteractionType());
			csvPrinter.write(failedEvent.getSourceModuleName());
			csvPrinter.write(failedEvent.getSourceComponentName());
			csvPrinter.write(failedEvent.getDestinationModuleName());
			csvPrinter.write(failedEvent.getDestinationComponentName());
			csvPrinter.write(failedEvent.getDestinationMethodName());
			csvPrinter.write(failedEvent.getResubmitDestination());
			SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_MILLS);
			csvPrinter.write(sdf.format(failedEvent.getFailureDateTime()));
			
			LOGGER.log(Level.FINE, "GETTING failureMessage");
			String failureMessage = failedEvent.getFailureMessage();

			// Truncating the failure message if it is larger than the maximum cell length
			if (failureMessage != null) {
				LOGGER.log(Level.FINE, "SHRINKING failureMessage");
				if (failureMessage.length() > Constants.REPORT_MAX_CELL_LENGTH) {

					failureMessage = failureMessage.substring(0, getMaximumReportCellSubstringSize()).concat(
							Constants.REPORT_TRUNCATE_STRING);
					csvPrinter.write(failureMessage);
				} else {
					csvPrinter.write(failureMessage);
				}
			} else { // write empty cell
				csvPrinter.write(failureMessage);
			}

			// Write parameters from the failed event
			StringBuilder sb = new StringBuilder();
			try {
				LOGGER.log(Level.FINE, "GETTING paramList");
				List paramList = null;
				
				/**
				 * PKFEIL-4692:
				 * I forbindelse med plattformoppgraderingen fra WPS 7.0 til BPM 8.5 ser det ut som disse kallene med
				 * adminClient.getConnectorProperties() sendt inn, bruker altfor lang tid i tillegg til å kaste exception.
				 * Ved å kalle getFailedEventParameters uten parametere, går kallet like fort som før, men kaster fortsatt exception.
				 * Avventer svar fra IBM gjennom PMR.
				 * 
				 * Update januar 2016:
				 * Skrevet workaround som setter et privat felt i en IBM-klasse til aksesserbar.
				 * Dette feltet inneholder input-dataobjektet i ren tekstformat. 
				 */
				if ((Constants.EVENT_TYPE_SCA).equals(event.getType())) {
					LOGGER.log(Level.FINE, "TYPE SCA - getting paramList");
					//paramList = ((SCAEvent) failedEvent).getFailedEventParameters(adminClient.getConnectorProperties());
					//paramList = ((SCAEvent) failedEvent).getFailedEventParameters();
					SCAEvent scaEvent = (SCAEvent) failedEvent;  
					paramList = getParams(scaEvent, event.getType());
					if (paramList == null) {
						LOGGER.log(Level.FINE, "Trying again - the original way");
						paramList = scaEvent.getFailedEventParameters();
					}
				}
				
				else if ((Constants.EVENT_TYPE_BPC).equals(event.getType())) {
					LOGGER.log(Level.FINE, "TYPE BPC - getting paramList");
					//paramList = ((BPCEvent) failedEvent).getInputMessage(adminClient.getConnectorProperties());
					//paramList = ((BPCEvent) failedEvent).getInputMessage();
					BPCEvent bpcEvent = (BPCEvent) failedEvent;
					paramList = getParams(bpcEvent, event.getType());
					if (paramList == null) {
						LOGGER.log(Level.FINE, "Trying again - the original way");
						paramList = bpcEvent.getInputMessage();
					}
				}
				
				else if ((Constants.EVENT_TYPE_JMS).equals(event.getType())) {
					LOGGER.log(Level.FINE, "TYPE JMS - getting paramList");
					//paramList = ((JMSEvent) failedEvent).getPayload(adminClient.getConnectorProperties());
					//paramList = ((JMSEvent) failedEvent).getPayload();
					JMSEvent jmsEvent = (JMSEvent) failedEvent;
					paramList = getParams(jmsEvent, event.getType());
					if (paramList == null) {
						LOGGER.log(Level.FINE, "Trying again - the original way");
						paramList = jmsEvent.getPayload();
					}
				}
				
				else if ((Constants.EVENT_TYPE_MQ).equals(event.getType())) {
					LOGGER.log(Level.FINE, "TYPE MQ - getting paramList");
					//paramList = ((MQEvent) failedEvent).getPayload(adminClient.getConnectorProperties());
					//paramList = ((MQEvent) failedEvent).getPayload();
					MQEvent mqEvent = (MQEvent) failedEvent;
					paramList = getParams(mqEvent, event.getType());
					if (paramList == null) {
						LOGGER.log(Level.FINE, "Trying again - the original way");
						paramList = mqEvent.getPayload();
					}
				}

				if (paramList != null) {
					LOGGER.log(Level.FINE, "ITERATE paramList");
					for (Iterator itBO = paramList.iterator(); itBO.hasNext();) {
						// Each parameter is know as a type of FailedEventParameter.
						FailedEventParameter failedEventParameter = (FailedEventParameter) itBO.next();

						if (failedEventParameter.getValue() instanceof DataObject) {
							LOGGER.log(Level.FINE, "INSTANCE OF DataObject");
							toString((DataObject) failedEventParameter.getValue(), sb);
						}
						
						else if (failedEventParameter.getValue() instanceof String) {
							LOGGER.log(Level.FINE, "INSTANCE OF String");
							sb.append(failedEventParameter.getValue().toString());
						}
						
						else if (failedEventParameter.getValue() instanceof byte[]) {
							// TODO: Hvordan forbedre denne? Det er ikke alltid gitt at tegnsett er av typen IBM-277
							LOGGER.log(Level.FINE, "INSTANCE OF byte[] - trying to convert with IBM-277");
							try {
								byte[] byteArray = (byte[])failedEventParameter.getValue();
								String convertedByteArray = new String(byteArray, "IBM-277");
								sb.append("BYTE-ARRAY CONVERTED WITH IBM-277 CHARACTER SET: " + convertedByteArray);
								LOGGER.log(Level.FINE, " - Converting byte-array with IBM-277 success.");
							} catch (RuntimeException e) {
								LOGGER.log(Level.FINE, " - Converting byte-array with IBM-277 failed.");
								sb.append("Unable to convert byte array");
							}
						}
						
						else {
							LOGGER.log(Level.FINE, "UNKNOWN INSTANCE - unable to convert");
							try {
								LOGGER.log(Level.FINE, " - Instance: " + failedEventParameter.getValue() + "(" + failedEventParameter.getValue().getClass().getSimpleName() + ")");
							} catch (RuntimeException e) {
								LOGGER.log(Level.FINE, " - Logging with extra info failed");
							}
							sb.append("Unable to convert");
						}
						// Ensure all 'cells' are filled to improve make the view even more easy to read
						sb.append(EMPTY);
					}
				}

			} catch (RuntimeException e) {
				String errormsg = "RuntimeException caught during retrieval of business object " + e.getClass().getName() + "("
						+ e.getMessage() + ")";
				sb.append(errormsg);
				sb.append(EMPTY);
				LOGGER.log(Level.SEVERE, errormsg, e);
			}
			csvPrinter.write(sb.toString());

			// Write PIID / CorrelationId
			csvPrinter.writeln(failedEvent.getCorrelationId());
			
			LOGGER.log(Level.FINE, "EXIT writeCSVEvent");
		}
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
					resultString.append(name);
					resultString.append("=");
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
	
	public void writeEventLineWithoutDetails(String informationMessage) throws IOException {
		// Write information message to report
		csvPrinter.writeln(informationMessage);
	}

	/**
	 * Returns the maximum length of a substring in order for the substring concatenated with the truncate string not to exceed
	 * the maximum cell length in the report.
	 * 
	 */
	public int getMaximumReportCellSubstringSize() {
		return Constants.REPORT_MAX_CELL_LENGTH - Constants.REPORT_TRUNCATE_STRING.length();
	}

	/**
	 * Method to get dataobject from a private field in one of IBMs classes.
	 * 
	 * @param event
	 * @param eventType
	 * @return
	 */
	private List getParams(FailedEvent event, String eventType) {
		List params = null;
		String fieldName = "failedEventParameters";
		if ((Constants.EVENT_TYPE_BPC).equals(eventType)) {
			fieldName = "parameters";
		}
		
		try {
			LOGGER.log(Level.FINE, "Use reflect for accessing private field");
			java.lang.reflect.Field f = event.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			params = (List) f.get(event);
			LOGGER.log(Level.FINE, "Success using reflect!");
		} catch (NoSuchFieldException e) {
			LOGGER.log(Level.FINE, "Reflect gave NoSuchFieldException: " + e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.FINE, "Reflect gave IllegalAccessException: " + e.getMessage());
			e.printStackTrace();
		}
		
		return params;
	}
}
