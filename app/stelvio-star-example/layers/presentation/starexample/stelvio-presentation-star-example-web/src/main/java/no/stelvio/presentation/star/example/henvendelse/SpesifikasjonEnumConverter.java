package no.stelvio.presentation.star.example.henvendelse;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.stelvio.domain.star.example.henvendelse.Spesifikasjon;

/**
 * @author personff564022aedd
 */
public class SpesifikasjonEnumConverter implements Converter {

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return Spesifikasjon.valueOf(value);
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((Spesifikasjon)value).toString();
	}

}
