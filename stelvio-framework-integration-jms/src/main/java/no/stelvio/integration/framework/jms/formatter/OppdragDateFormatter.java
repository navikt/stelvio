package no.stelvio.integration.framework.jms.formatter;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * Date formatter used within castor mapping file.
 * 
 * @author persone5d69f3729a8, Accenture
 * @version $Id: OppdragDateFormatter.java 2782 2006-02-24 11:54:05Z skb2930 $
 * @todo Use DateUtil
 */
public class OppdragDateFormatter extends GeneralizedFieldHandler {
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Default constructor.
	 */
	public OppdragDateFormatter() {
	}

	/**
	 * This method is used to convert the value (from java.util.Date to String) when the
	 * the getValue method is called.
	 * The getValue method will obtain the actual field value from given 'parent' object.
	 * This convert method is then invoked with the field's value. The value returned
	 * from this method will be the actual returned by getValue method.
	 *  
	 * @param value the object to convert after performing a get operation
	 */
	public Object convertUponGet(Object value) {
		if (null == value) {
			return null;
		} else {
			return createDateFormatter().format((Date) value);
		}
	}

    /** 
     * This method is used to convert the value (from String to 
     * java.util.Date) when the setValue method is called. 
     * The setValue method will call this method to obtain 
     * the converted value. The converted value will then be used as 
     * the value to set for the field. 
     * 
     * @param value the object value to convert before performing a set 
     * operation 
     * @return the converted value. 
     */ 
	public Object convertUponSet(Object value) {
		return createDateFormatter().parseObject((String) value, new ParsePosition(0));
	}

    /** 
     * Returns the class type for the field that this GeneralizedFieldHandler 
     * converts to and from. This should be the type that is used in the 
     * object model. 
     * 
     * @return the class type of of the field 
     */ 
	public Class getFieldType() {
		return Date.class;
	}

	/**
	 * Creates a date formatter.
	 *
	 * @return a date formatter.
	 */
	private DateFormat createDateFormatter() {
		final SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getInstance();
		dateFormat.setLenient(false);
		dateFormat.applyLocalizedPattern(DATE_FORMAT);

		return dateFormat;
	}
}
