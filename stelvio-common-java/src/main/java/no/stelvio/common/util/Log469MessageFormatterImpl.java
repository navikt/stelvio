package no.stelvio.common.util;

import java.net.InetAddress;
import java.util.Date;

import no.stelvio.common.error.support.Severity;

/**
 * Formats messages for the 469 Log System.
 * 
 * @author person356941106810, Accenture
 * @version $Id: Log469MessageFormatterImpl.java 2817 2006-03-02 11:59:19Z skb2930 $
 */
public class Log469MessageFormatterImpl implements MessageFormatter {

	private final String systemId;
	private final String systemLanguage;
	private final String systemName;

	private final String datePattern;
	private final String timePattern;

	/**
	 * Creates a MessageFormatter that produces messages that are suitable for the 469 logging system.
	 * @param systemId the system id of the application
	 * @param systemLanguage the language the application uses 
	 * @param systemName the name of the system
	 * @param datePattern the desired date pattern
	 * @param timePattern the desired time pattern
	 */
	public Log469MessageFormatterImpl(
		String systemId,
		String systemLanguage,
		String systemName,
		String datePattern,
		String timePattern) {
		this.systemId = systemId;
		this.systemLanguage = systemLanguage;
		this.datePattern = datePattern;
		this.timePattern = timePattern;

		// Added host address for easy identification of error location in multiple environments
		String hostAddress = null;
		try {
			hostAddress = systemName + " (" + InetAddress.getLocalHost().getHostAddress() + ")";
		} catch (Exception e) {
			hostAddress = systemName;
		}

		this.systemName = hostAddress;
	}

	/**
	 * Formats a message that is suitable for the 469 log system,
	 * 
	 * Input array must have a length og 5 and the elements must be as follows:<br>
	 * [0] = userId. Can be null. If null, ANON will be used
	 * [1] = the log severity. If null it will default to "I" 
	 * [2] = the screenId. If null it will default to "UNKNOWN"
	 * [3] = 'funksjonsOpphav'. Defaults to a zero length string if null
	 * [4] = the log message
	 * 
	 * @see no.stelvio.common.util.MessageFormatter#formatMessage(java.lang.Object[])
	 * @param params the arguments to be formatted with the message.
	 * @return the formatted message
	 */
	public String formatMessage(Object[] params) {
		Date now = new Date(System.currentTimeMillis());

		StringBuffer sb = new StringBuffer();
		// =================================
		// Parameter         Format   Logges
		// =================================
		// System-id              8   ja
		sb.append(getFixedLength(systemId, 8, false));

		// Dato-opprinnelse       8   ja
		sb.append(getFixedLength(DateUtil.createDateFormat(datePattern).format(now), 8, false));

		// Tid-opprinnelse        6   ja
		sb.append(getFixedLength(DateUtil.createDateFormat(timePattern).format(now), 6, false));

		// Bruker-id              8   ja
		String userId = (String) params[0];
		if (userId == null || "".equals(userId.trim())) {
			userId = "ANON";
		}
		sb.append(getFixedLength(userId, 8, false));

		//	Logg-nivaa             1   ja
		sb.append(getLogLevel((Integer) params[1]));

		//	Program-spraak         8   ja
		sb.append(getFixedLength(systemLanguage, 8, false));

		//	Program-id-logg       30   ja
		sb.append(getFixedLength(systemName, 30, false));

		//	Program-id-opphav     30   ja
		String screenId = (String) params[2];
		if (screenId == null || "".equals(screenId.trim())) {
			screenId = "UNKNOWN";
		}
		sb.append(getFixedLength(screenId, 30, false));

		//	Funksjon-opphav       30   ja
		sb.append(getFixedLength((String) params[3], 30, false));

		//	DB-retur-kode          4   nei
		sb.append(getFixedLength(null, 4, false));

		//	DB-retur-status        5   nei
		sb.append(getFixedLength(null, 5, false));

		//	DB-retur-melding      80   nei
		sb.append(getFixedLength(null, 80, false));

		//	MQ-kode                4   nei
		sb.append(getFixedLength(null, 4, false));

		//	MQ-aarsak              4   nei
		sb.append(getFixedLength(null, 4, false));

		//	Logg-melding         255   ja
		sb.append(getFixedLength((String) params[4], 255, true));
		// =================================

		return sb.toString();
	}

	/**
	 * Copies the contents of the String into a fixed length char array.
	 * If the length of the String is smalle than length, the remaining characters 
	 * will be padded with spaces. If truncate is specified, the last three characters
	 * of the char array will be replaced with dots (Eg. "This string was to long for ..."). 
	 * 
	 * @param field the text to copy
	 * @param length the fixed length
	 * @param truncate true to truncate, false otherwise
	 * @return the fixed length char array
	 */
	private char[] getFixedLength(String field, int length, boolean truncate) {

		final String text = (null == field ? "" : field);
		final char[] c = new char[length];
		final int textLength = (text.length() < length ? text.length() : length);

		// Copy contents of the String
		for (int i = 0; i < textLength; i++) {
			c[i] = field.charAt(i);
		}
		// Padding if field.length() is less than length
		for (int i = textLength; i < c.length; i++) {
			c[i] = ' ';
		}
		if (truncate) {
			// Don't replace with dots if string is short
			if (5 < c.length) {
				if (c[c.length - 1] != ' ' || c[c.length - 2] != ' ' || c[c.length - 3] != ' ') {
					c[c.length - 1] = '.';
					c[c.length - 2] = '.';
					c[c.length - 3] = '.';
				}
			}
		}
		return c;
	}

	/**
	 * Returns the logg-nivaa that will be logged to the 469 log system.
	 * 
	 * @param severity the severity
	 * @return the logg-nivaa 
	 */
	private String getLogLevel(Integer severity) {
		if (Severity.FATAL.getLevel() == severity) {
			return "S";
		} else if (Severity.ERROR.getLevel() == severity) {
			return "E";
		} else if (Severity.WARN.getLevel() == severity) {
			return "W";
		} else {
			return "I";
		}
	}
}
