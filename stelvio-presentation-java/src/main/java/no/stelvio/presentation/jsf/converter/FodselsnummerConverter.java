package no.stelvio.presentation.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Adds or removes a space in fodselsnummer.
 * 
 */
public class FodselsnummerConverter implements Converter {

	/**
	 * {@inheritDoc}
	 * @param context
	 * 					FacesContext
	 * @param component
	 * 					UIComponent
	 * @param value
	 * 					Inncomming value to convert.
	 * @return returns fnr where all whitespaces are removed.
	 * @throws ConverterException
	 * 					ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		
		String convertedString = null;
		
		// Dummy implementation - improve before using
		if (value.length() == 12) {
			convertedString = value.replaceAll(" ", "");
		}
		return (String) convertedString;
	}

	/**
	 * {@inheritDoc}
	 * @param context
	 * 					FacesContext
	 * @param component
	 * 					UIComponent
	 * @param value
	 * 					Inncomming value to convert.
	 * @return fnr with added whitespace.
	 * @throws ConverterException
	 * 					ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return value != null && value instanceof String && ((String) value).length() == 11 
		? ((String) value).substring(0, 6) + " " + ((String) value).substring(6) : "";
	}
}