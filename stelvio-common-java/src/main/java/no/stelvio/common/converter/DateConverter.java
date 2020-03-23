package no.stelvio.common.converter;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import no.stelvio.common.util.DateUtil;

/**
 * Converter for calendar objects.
 * 
 * @version $Id: DateConverter.java 2565 2005-10-19 15:19:43Z psa2920 $
 */
public class DateConverter implements Converter {

	/**
	 * This method does the convertion to a Date.
	 * @param type - Type to convert to
	 * @param value - Object of given type
	 * @return object - Date object.
	 */
	@Override
	public Object convert(Class type, Object value) {
		if (null == value) {
			return null;
		} else if (value instanceof Date) {
			return value;
		} else if (value instanceof Calendar) {
			return ((Calendar) value).getTime();
		} else {
			try {
				return DateUtil.parse(value.toString());
			} catch (IllegalArgumentException e) {
				throw new ConversionException("Can not convert from " + type.getName() + " to java.util.Date", e);
			}
		}
	}

}
