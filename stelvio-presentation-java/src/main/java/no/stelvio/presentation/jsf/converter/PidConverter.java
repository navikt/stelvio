package no.stelvio.presentation.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Pid Converter.
 * 
 * @see javax.faces.convert.Converter
 * @version $Id$
 */
public class PidConverter implements Converter {
	/**
	 * {@inheritDoc} Get as Pid as Object.
	 * 
	 * @see javax.faces.context.FacesContext
	 * @see javax.faces.component.UIComponent
	 * @see javax.faces.convert.ConverterException
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value.
	 * @return Object stripped of whitespace.
	 * @throws ConverterException
	 *             If convertion goes wrong.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return value.replaceAll(" ", "").toString();
	}

	/**
	 * {@inheritDoc} Get as Pid as String.
	 * 
	 * @see javax.faces.context.FacesContext
	 * @see javax.faces.component.UIComponent
	 * @see javax.faces.convert.ConverterException
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value.
	 * @return pid as String, "" if value is empty.
	 * @throws ConverterException
	 *             If convertion goes wrong.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		if (value != null) {
			StringBuffer pidFormatted = new StringBuffer((String) value);
			if (pidFormatted.charAt(6) != ' ') {
				pidFormatted.insert(6, ' ');
			}
			return pidFormatted.toString();
		}
		return "";
	}

}
