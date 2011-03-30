package no.nav.sibushelper.filewriters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.nav.sibushelper.SIBUSHelper;
import no.nav.sibushelper.common.Constants;
import no.nav.sibushelper.helper.MessageInfo;

import org.apache.commons.lang.StringUtils;

/**
 * This class that writes events to file
 * 
 * @author persona2c5e3b49756 Schnell
 */

public class MessageFileWriter extends AbstractFileWriter {
	/**
	 * Logger instance
	 */
	private Logger LOGGER = Logger.getLogger(SIBUSHelper.class.getName());

	/**
	 * Default parameterized constructor
	 * 
	 * @param path
	 *            path
	 * @param filename
	 *            filename
	 * @throws IOException
	 */
	public MessageFileWriter(String path, String filename) throws IOException {
		if (StringUtils.isEmpty(path)) {
			String tempFolderProperty = "java.io.tmpdir";
			String userFolderProperty = "user.dir";
			String userFolder = System.getProperty(userFolderProperty);
			if (StringUtils.isEmpty(userFolder)) {
				userFolder = System.getProperty(tempFolderProperty);
			}

			path = userFolder;
		}
		String completePath = path + File.separatorChar + filename;
		LOGGER.log(Level.FINE, "Creating instance of BufferedWriter with path: " + completePath);
		writer = new BufferedWriter(new FileWriter(completePath, true));
	}

	/**
	 * write the header in the csv file
	 */
	public void writeHeader() throws IOException {
		writer.write("Queue" + separator);
		writer.write("BusName" + separator);
		writer.write("MessageId" + separator);
		writer.write("ApplicationMessageId" + separator);
		writer.write("CorrelationId" + separator);
		writer.write("ApiUserId" + separator);
		writer.write("SysUserId" + separator);
		writer.write("RedeliveredCount" + separator);
		writer.write("Reliability" + separator);
		writer.write("MessageType" + separator);
		writer.write("MessageBodyType" + separator);
		writer.write("ExceptionMessage" + separator);
		writer.write("ExceptionTimestamp" + separator);
		writer.write("ExceptionReason" + separator);
		writer.write("ProblemDestination" + separator);
		writer.write("ProblemDestinationBus" + separator);
		writer.write("ApproximateLength" + separator);
		writer.write("MessageTimestamp" + separator);
		writer.write("MessageMEArrivalTimestamp" + separator);
		writer.write("MessageWaitTimestamp" + separator);
		writer.write("MessageData");
		writer.newLine();
		writer.flush();
	}

	/**
	 * write the header in the csv file
	 */
	public void writeSEHeader() throws IOException {
		writer.write("Queue" + separator);
		writer.write("Status" + separator);
		writer.write("BusName" + separator);
		writer.write("MessageId" + separator);
		writer.write("ApplicationMessageId" + separator);
		writer.write("CorrelationId" + separator);
		writer.write("ApiUserId" + separator);
		writer.write("SysUserId" + separator);
		writer.write("RedeliveredCount" + separator);
		writer.write("Reliability" + separator);
		writer.write("MessageType" + separator);
		writer.write("MessageBodyType" + separator);
		writer.write("ExceptionMessage" + separator);
		writer.write("ExceptionTimestamp" + separator);
		writer.write("ExceptionReason" + separator);
		writer.write("ProblemDestination" + separator);
		writer.write("ProblemDestinationBus" + separator);
		writer.write("ApproximateLength" + separator);
		writer.write("MessageTimestamp" + separator);
		writer.write("MessageMEArrivalTimestamp" + separator);
		writer.write("MessageWaitTimestamp" + separator);
		writer.write("MessageData");
		writer.newLine();
		writer.flush();
	}

	/**
	 * Format from Message format to CSV and write
	 * 
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeCSVMessage(MessageInfo message, String queueName) throws IOException {

		// Write information about this event
		writer.write(getEscapedString(queueName) + separator);
		writer.write(getEscapedString(message.getBusName()) + separator);
		writer.write(getEscapedString(message.getSystemMessageId()) + separator);
		writer.write(getEscapedString(message.getApiMessageId()) + separator);
		writer.write(getEscapedString(message.getCorrelationId()) + separator);
		writer.write(getEscapedString(message.getApiUserId()) + separator);
		writer.write(getEscapedString(message.getSysUserId()) + separator);
		writer.write(getEscapedString(new Integer(message.getRedeliveredCount()).toString()) + separator);
		writer.write(getEscapedString(message.getReliability()) + separator);
		writer.write(getEscapedString(message.getMessageType()) + separator);
		writer.write(getEscapedString(message.getMessageBodyType()) + separator);
		writer.write(getEscapedString(message.getExceptionMessage()) + separator);
		writer.write(getEscapedString(getTimestamp(message.getExceptionTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		writer.write(getEscapedString(new Integer(message.getExceptionReason()).toString()) + separator);
		writer.write(getEscapedString(message.getOrigDestination()) + separator);
		writer.write(getEscapedString(message.getOrigDestinationBus()) + separator);
		writer.write(getEscapedString(new Integer(message.getApproximateLength()).toString()) + separator);
		writer.write(getEscapedString(getTimestamp(message.getCurrentTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		writer.write(getEscapedString(getTimestamp(message.getCurrentMEArrivalTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		long elapsed = message.getCurrentMEArrivalTimestamp() + message.getCurrentMessageWaitTimestamp();
		writer.write(getEscapedString(getElapsedTime(elapsed)) + separator);
		// Write quotes start. This is needed to get all values within
		// the same cell if and when this data is imported in Excel.
		writer.write("\"");
		String messageData = getEscapedString(message.getMsgStringBuffer().toString());
		if(messageData != null) {
			if (messageData.length() > Constants.REPORT_MAX_CELL_LENGTH) {

				messageData = messageData.substring(0, getMaximumReportCellSubstringSize()).concat(
						Constants.REPORT_TRUNCATE_STRING);
				writer.write(messageData);
			} else {
				writer.write(messageData);
			}
		} else { // write empty cell
			writer.write(messageData);
		}

		// Write quotes end plus separator
		writer.write("\"" + separator);
		// Write a empty line the end of this entry and close the writer
		writer.newLine();
		writer.flush();
	}

	/**
	 * Format from Message format to CSV and write
	 * 
	 * @param event
	 * @param parameters
	 * @param adminClient
	 * @throws IOException
	 */
	public void writeCSVSEMessage(MessageInfo message, String queueName) throws IOException {

		// Write information about this event
		writer.write(getEscapedString(queueName) + separator);
		writer.write(getEscapedString(message.getStatus()) + separator);
		writer.write(getEscapedString(message.getBusName()) + separator);
		writer.write(getEscapedString(message.getSystemMessageId()) + separator);
		writer.write(getEscapedString(message.getApiMessageId()) + separator);
		writer.write(getEscapedString(message.getCorrelationId()) + separator);
		writer.write(getEscapedString(message.getApiUserId()) + separator);
		writer.write(getEscapedString(message.getSysUserId()) + separator);
		writer.write(getEscapedString(new Integer(message.getRedeliveredCount()).toString()) + separator);
		writer.write(getEscapedString(message.getReliability()) + separator);
		writer.write(getEscapedString(message.getMessageType()) + separator);
		writer.write(getEscapedString(message.getMessageBodyType()) + separator);
		writer.write(getEscapedString(message.getExceptionMessage()) + separator);
		writer.write(getEscapedString(getTimestamp(message.getExceptionTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		writer.write(getEscapedString(new Integer(message.getExceptionReason()).toString()) + separator);
		writer.write(getEscapedString(message.getOrigDestination()) + separator);
		writer.write(getEscapedString(message.getOrigDestinationBus()) + separator);
		writer.write(getEscapedString(new Integer(message.getApproximateLength()).toString()) + separator);
		writer.write(getEscapedString(getTimestamp(message.getCurrentTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		writer.write(getEscapedString(getTimestamp(message.getCurrentMEArrivalTimestamp(), Constants.DEFAULT_DATE_FORMAT_TZ))
				+ separator);
		long elapsed = message.getCurrentMEArrivalTimestamp() + message.getCurrentMessageWaitTimestamp();
		writer.write(getEscapedString(getElapsedTime(elapsed)) + separator);
		// Write quotes start. This is needed to get all values within
		// the same cell if and when this data is imported in Excel.
		writer.write("\"");
		writer.write(getEscapedString(message.getMsgStringBuffer().toString()));
		// Write quotes end plus separator
		writer.write("\"" + separator);
		// Write a empty line the end of this entry and close the writer
		writer.newLine();
		writer.flush();
	}

	/**
	 * TODO: might be more complexity is necessary to filter out other chars tha
	 * sep. Returns a string with all <code>;</code> characters replace it
	 * with <code>#</code>
	 * 
	 * @param s
	 *            String to replace
	 * @return fixed string
	 */
	private String getEscapedString(String s) {
		if (s != null) {
			String result = s.replaceAll(";", "#");
			// double tegn we don't need
			result = result.replaceAll("##", "#");

			// " tegn we have to convert due to excel
			result = result.replaceAll("\"", "'");

			return result;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * @param time
	 * @return
	 */
	public String getTimestamp(Long time, String dateFormat) {
		if (time == null) {
			return "";
		} else {
			Date date = new Date(time.longValue());
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
			formatter.setTimeZone(TimeZone.getDefault());
			return formatter.format(date);
		}
	}

	/**
	 * @param time
	 * @return
	 */
	public String getElapsedTime(Long time) {
		if (time == null || time == 0) {
			return "";
		} else {
			long hours, minutes, seconds;
			long systime = System.currentTimeMillis();
			long timeInSeconds = (systime - time) / 1000;
			hours = timeInSeconds / 3600;
			seconds = timeInSeconds - (hours * 3600);
			minutes = seconds / 60;
			String ret = Long.toString(hours) + " hour(s) " + Long.toString(minutes) + " minutes " + Long.toString(seconds)
					+ " seconds";
			return ret;
		}
	}
	
	/**
	 * Returns the maximum length of a substring in order for the substring concatenated with the truncate string not to exceed
	 * the maximum cell length in the report.
	 * 
	 */
	public int getMaximumReportCellSubstringSize() {
		return Constants.REPORT_MAX_CELL_LENGTH - Constants.REPORT_TRUNCATE_STRING.length();
	}
}
