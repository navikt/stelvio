package no.stelvio.presentation.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Converts a large number to thousand shorthand forma, i.e, the 
 * number 355 000 gets converted to the format 355'.
 * 
 * @author person7c5197dbb870 (Capgemini)
 */
public class ThousandConverter implements Converter {
	/**
	 * {@inheritDoc}
	 * 
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return returns the Number represented by this shorthand string 
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {

		Number number = null;

		if (value != null && value.endsWith("'")) {
			StringBuffer sb = new StringBuffer(value);
			sb.deleteCharAt(value.length() - 1);
			sb.append("000");
			try {
				number = Integer.parseInt(sb.toString());
			} catch (NumberFormatException e) {
				throw new ConverterException(e.getMessage(), e.getCause());
			}
		}
		return number;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return the shorthand string representation of this value
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		Integer number = null;
		if (value instanceof Number) {
			number = (Integer) value;
			if (number > 1000) {
				number = number / 1000;
				return number.toString() + "'";
			}
		}
		return null;

	}

}