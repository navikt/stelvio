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
	 * Converts from a String to a Spesifikasjon.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return Spesifikasjon.valueOf(value);
	}

	/**
	 * Converts from a Spesifikasjon to a String.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return value.toString();
	}

}
