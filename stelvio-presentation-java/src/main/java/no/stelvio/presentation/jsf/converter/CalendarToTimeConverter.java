package no.stelvio.presentation.jsf.converter;

import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * <code>CalendarToTimeConverter</code> gets the time (hh.mm.ss) from a <code>Calendar</code> instance.
 * 
 * @author personedfee2afc85c
 */
public class CalendarToTimeConverter implements Converter {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.String)
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return Get <code>value</code> as Object.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.Object)
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return Get <code>value</code> as String.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if (value == null) {
			return "";
		}
		Calendar cal = (Calendar) value;

		int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
		StringBuffer time = new StringBuffer();
		if (hourOfDay < 10) {
			time.append("0");
		}
		time.append(hourOfDay);
		time.append(".");

		int minute = cal.get(Calendar.MINUTE);
		if (minute < 10) {
			time.append("0");
		}
		time.append(minute);
		time.append(".");

		int second = cal.get(Calendar.SECOND);
		if (second < 10) {
			time.append("0");
		}
		time.append(second);

		return time.toString();
	}

}
