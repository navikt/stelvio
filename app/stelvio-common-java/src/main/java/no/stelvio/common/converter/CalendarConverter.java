package no.stelvio.common.converter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

/**
 * 
 * Converter for calendar objects.
 * 
 * @author person5b7fd84b3197, Accenture
 * @version $Id: CalendarConverter.java 2572 2005-10-19 16:13:41Z psa2920 $
 */
public class CalendarConverter implements Converter {

	/**
	 * Converts a Date object to a Calendar object.
	 * 
	 * {@inheritDoc}
	 * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
	 */
	public Object convert(Class clazz, Object object) {
		if (null == object) {
			return null;
		} else if (object instanceof Calendar) {
			return object;
		} else if (object instanceof Date) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) object);
			return calendar;
		} else {
			throw new ConversionException("Can not convert from " + clazz.getName() + " to java.util.Calendar");
		}
	}

}
