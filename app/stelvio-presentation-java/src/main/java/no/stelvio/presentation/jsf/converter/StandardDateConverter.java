package no.stelvio.presentation.jsf.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.stelvio.common.util.DateUtil;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Class StandardDateConverter can be used for Standard date conversion.
 * It returns the validation message PP009 from the message source defined as <message-bundle> in faces-config
 * populated with field label and default date format.
 * The validation is done by the no.stelvio.common.util.DateUtil.parseInputString(String, boolean) method.
 * This is how you should use this class in your jsf-file (e.g. .xhtml/.jsf/.jsp):
 * <p><blockquote><pre>
 * 	<h:inputText id="dateId" value="#{flowScope.date}" > <!-- Normal JSF input text field -->
 * 		<f:converter converterId="dateConverter" /> <!-- The dateConverter defined in faces-config -->
 * 		<f:attribute name="label" value="#{label}"/> <!-- The messageSource reference to the field label  -->
 * 	</h:inputText>
 * </pre></blockquote></p>
 *
 * @author persone8306a256e67
 * @version $Id$
 * @see no.stelvio.common.util.DateUtil
 */
public class StandardDateConverter implements Converter {

	
	private static final String PATTERN = "dd.MM.yyyy";

	private static final String BUNDLE_NAME = "no-nav-pensjon-pselv-resources";

	private static final String PSELV_VALIDATION_DATOFORMAT = "pselv.standardvalidering.datoformat";

	private static final String PSELV_LEDETEKST_DATOFORMAT = "pselv.standardtekst.ledetekst.datoformat";

	private static final String PSAK_VALIDATION_DATOFORMAT = "validation.datoformat";

	private static final String PSAK_LEDETEKST_DATOFORMAT = "standard.ledetekst.datoformat";

	//Attribute name for label text
	private static final String LABEL = "label";

	private ResourceBundleMessageSource messageSource;

	/**
	 * Creates new instance with a messageSource for handling i18n.
	 */
	public StandardDateConverter() {
		messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(BUNDLE_NAME);
	}

	/**
	 * @inheritDoc {javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *             javax.faces.component.UIComponent, java.lang.String)}
	 * @param context
	 * 					FacesContext
	 * @param component
	 * 					UIComponent
	 * @param value
	 * 					Inncomming value to convert.
	 * @return convert String to Date.
	 * @throws ConverterException
	 * 					ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return (value == null) || ("".equals(value)) ? null : convertStringToDate(value, component, context);
	}

	/**
	 * @inheritDoc {javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *             javax.faces.component.UIComponent, java.lang.Object)}
	 * @param context
	 * 					FacesContext
	 * @param component
	 * 					UIComponent
	 * @param value
	 * 					Inncomming value to convert.
	 * @return convert Date to String.
	 * @throws ConverterException
	 * 					ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((value == null) || !(value instanceof Date)) ? "" : convertDateToString((Date) value, context);
	}

	/**
	 * Converts from Date to string. 
	 * 
	 * @param date the Date to be converted
	 * @param context FacesContext
	 * @return String version of the date
	 */
	private String convertDateToString(Date date, FacesContext context) {
		return new SimpleDateFormat(PATTERN, getLocale(context)).format(date);
	}

	/**
	 * Convert from string date to Date object. Makes a error message in case of wrong date format.
	 * 
	 * @param stringDate string version of the date
	 * @param component the associated UIComponent
	 * @param context FacesContext
	 * @return the date object after conversion
	 */
	private Date convertStringToDate(String stringDate, UIComponent component, FacesContext context) {

		try {

			return DateUtil.parseInputString(stringDate, true);

		} catch (IllegalArgumentException e) {

			String summary = createMessage(component);
			FacesMessage msg = new FacesMessage(summary, e.getMessage());
			context.addMessage(component.getClientId(context), msg);

		}
		return null;
	}

	/**
	 * Create an error message.
	 * 
	 * @param component the associated UIComponent
	 * @return the error message
	 */
	private String createMessage(UIComponent component) {

		String datoformat = messageSource.getMessage(PSELV_LEDETEKST_DATOFORMAT, null, LocaleContextHolder.getLocale());
		if (datoformat == null) {
			datoformat = messageSource.getMessage(PSAK_LEDETEKST_DATOFORMAT, null, LocaleContextHolder.getLocale());
		}

		String fieldLabel = (String) component.getAttributes().get(LABEL);
		//Handle that the JSF component does not give a label.
		if (fieldLabel == null) {
			fieldLabel = "";
		}
		String[] params = new String[2];
		params[0] = fieldLabel;
		params[1] = datoformat;
		String summary = messageSource.getMessage(PSELV_VALIDATION_DATOFORMAT, params, LocaleContextHolder.getLocale());
		if (summary == null) {
			summary = messageSource.getMessage(PSAK_VALIDATION_DATOFORMAT, params, LocaleContextHolder.getLocale());
		}

		return summary;

	}

	/**
	 * Returns the current locale.
	 * 
	 * @param context FacesContext
	 * @return the current locale
	 */
	private Locale getLocale(FacesContext context) {
		return context.getViewRoot().getLocale();
	}

}