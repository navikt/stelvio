package no.nav.presentation.pensjon.saksbehandling.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class FnrConverter implements Converter
{

	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		// TODO Auto-generated method stub
		
		System.out.println( "-------getAsObject---------" );
		System.out.println( "component = " + component );
		System.out.println( "value = " + value );
		
		if( value.length() == 12 )
		{
			value = value.replaceAll(" ", "");
		}
		
		System.out.println( "value = " + value );
		
		System.out.println( "-------end getAsObject---------" );
		return new String( value );
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		// TODO Auto-generated method stub
		System.out.println( "-------getAsString---------" );
		
		System.out.println( "component = " + component );
		System.out.println( "value = " + value );
		
		String rValue = "";
		
		if( value != null )
		{
			rValue = (String) value;
			rValue = rValue.substring(0, 6) + " " + rValue.substring(6);
		}
		
		System.out.println( "-------end getAsString---------" );
		return rValue;
	}
	
}