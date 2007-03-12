package no.stelvio.presentation.star.example.henvendelse;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.stelvio.domain.star.example.henvendelse.Tidsperiode;

/**
 * @author personff564022aedd
 */
public class TidsperiodeEnumConverter implements Converter {

	/**
	 * Converts from a String to a Tidsperiode.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		// TODO i18n
		if (value == null || value.equals("Velg tidsperiode:")) {
			return Tidsperiode.IKKE_SATT;
		}
		
		return Tidsperiode.valueOf(value);
	}

	/**
	 * Converts from a Tidsperiode to a String.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		// hack to support enums in form
		// TODO i18n
		if (value == null || value instanceof String) {
			return "Velg tidsperiode:";
		}

		return value.toString();
	}
}
