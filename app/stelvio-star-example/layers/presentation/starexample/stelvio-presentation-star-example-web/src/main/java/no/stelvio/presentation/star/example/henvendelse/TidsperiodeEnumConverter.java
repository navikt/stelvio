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
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		if (value == null || value.equals("Velg tidsperiode:")) {
			return Tidsperiode.IKKE_SATT;
		}
		return Tidsperiode.valueOf(value);
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		// hack to support enums in form
		if (value == null || value instanceof String) {
			return "Velg tidsperiode:";
		}
		return ((Tidsperiode)value).toString();
	}
}
