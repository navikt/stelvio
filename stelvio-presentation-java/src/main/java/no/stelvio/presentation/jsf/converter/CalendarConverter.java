package no.stelvio.presentation.jsf.converter;

import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Calendar converter.
 * 
 */
public class CalendarConverter implements Converter {

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
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		String dayOfMonthShow = String.valueOf(dayOfMonth);
		if (dayOfMonth < 10) {
			dayOfMonthShow = "0" + dayOfMonthShow;
		}

		int month = cal.get(Calendar.MONTH) + 1;
		String monthShow = String.valueOf(month);
		if (month < 10) {
			monthShow = "0" + monthShow;
		}

		return dayOfMonthShow + "." + monthShow + "." + cal.get(Calendar.YEAR);
	}

}
