package no.stelvio.integration.framework.hibernate.formater;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import no.stelvio.common.framework.FrameworkError;
import no.stelvio.common.framework.error.SystemException;

/**
 * 
 * @author person356941106810, Accenture
 */
public class DateFormater implements Formater {
	private String formatPattern = null;
	
	/**
	 * {@inheritDoc}
	 * <p/>
	 * Formats a date into a date string.
	 */
	public String formatInput(Object input) {
		if (null != input) {
			return createDateFormat().format((Date) input);
		}
		return "";
	}

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Parses a string and returns a Date.
	 */
	public Object formatOutput(String output) {
		Date result = null;
		if (null != output && !"".equals(output.trim())) {
			try {
				result = createDateFormat().parse(output);
			} catch (ParseException e) {
				throw new SystemException(FrameworkError.SERVICE_TYPE_ERROR, e);
			}
		}
		return result;
	}
	
	/**
	 * Sets the format patter for BBM.
	 * 
	 * @param string the pattern
	 */
	public void setFormatPattern(String string) {
		formatPattern = string;
	}

	/**
	 * Cretaes a date formatter.
	 *
	 * @return a date formatter.
	 */
	private DateFormat createDateFormat() {
		final SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.setLenient(false);
		dateFormat.applyLocalizedPattern(formatPattern);

		return dateFormat;
	}
}
