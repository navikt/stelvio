package no.stelvio.presentation.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Used to convert enums to strings and back again. Used in dropdownlists that contains enums.
 * 
 * @version $Id$
 */
public class EnumConverter implements Converter {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		Class enumType = component.getValueBinding("value").getType(context);
		return Enum.valueOf(enumType, value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		if (object == null) {
			return null;
		}
		Enum type = (Enum) object;
		return type.toString();
	}
}
