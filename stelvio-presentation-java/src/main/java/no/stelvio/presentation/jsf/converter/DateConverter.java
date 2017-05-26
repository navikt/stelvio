package no.stelvio.presentation.jsf.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Class DateConverter could be used to get customized conversion error messages in jsf. This is how you should use this class
 * in your jsf-file (e.g. .xhtml/.jsf/.jsp):
 * <p>
 * <blockquote>
 * 
 * <pre>
 *         	&lt;h:inputText id=&quot;dateId&quot; value=&quot;#{flowScope.date}&quot; &gt; &lt;!-- Normal JSF input text field --&gt;
 *         		&lt;f:converter converterId=&quot;dateConverter&quot; /&gt; &lt;!-- The dateConverter defined in faces-config --&gt;
 *         		&lt;f:attribute name=&quot;pattern&quot; value=&quot;ddMMyyyy&quot; /&gt; &lt;!-- The date pattern/format. It also support several date formats (most preferrable first), e.g.: value=&quot;ddMMyyyy;dd.MM.yy;ddMMyy&quot;  --&gt;
 *         		&lt;f:attribute name=&quot;errorMsg&quot; value=&quot;validation.datoformat&quot; /&gt; &lt;!-- Reference to the error message in the resource bundle --&gt;
 *         		&lt;f:attribute name=&quot;fieldName&quot; value=&quot;person.sokeperson.ledetekst.fodselsdato&quot; /&gt; &lt;!-- Reference to the {0}-argument for the error message --&gt;
 * 	        	&lt;f:attribute name=&quot;localePattern&quot; value=&quot;person.sokeperson.statisktekst.ddMmAaaa&quot; /&gt; &lt;!-- Reference to the {1}-argument for the error message --&gt;
 *         	&lt;/h:inputText&gt;
 * </pre>
 * 
 * </blockquote>
 * </p>
 * The MessageSource will be retrieved from the application context if not defined.
 * 
 * @author persone38597605f58 (NAV)
 * @version $Id$
 */
public class DateConverter implements Converter {

	static final String PATTERN = "pattern";

	static final String ERROR_MSG = "errorMsg";

	static final String FIELD_NAME = "fieldName";

	static final String LOCALE_PATTERN = "localePattern";

	static final String VALID_CHARACTERS = "0123456789.-/\\";

	static final String VALID_DIVIDE_CHARACTERS = ".-/\\";

	/**
	 * This class' logger. Uses a Utility class to perform the logging, since it is required - and best practice - to always
	 * check for the log level before actually performing the logging.
	 */
	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.String)
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return String converted to Date.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
		return StringUtils.isBlank(value) ? null : convertStringToDate(StringUtils.trim(value), component);
	}

	/**
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent,
	 *      java.lang.Object)
	 * @param context
	 *            FacesContext
	 * @param component
	 *            UIComponent
	 * @param value
	 *            Inncomming value to convert.
	 * @return Date converted to String.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((value == null) || !(value instanceof Date)) ? "" : convertDateToString((Date) value, component);
	}

	/**
	 * Converts from Date to string. It will always return the date on the most preferrable format. This is indicated by using
	 * the first element in the array.
	 * 
	 * @param date
	 *            the Date to be converted
	 * @param component
	 *            the associated UIComponent
	 * @return String version of the date
	 */
	private String convertDateToString(Date date, UIComponent component) {
		String[] dateFormatArray = getDateFormatArray(getStringAttribute(component, PATTERN));
		return new SimpleDateFormat(dateFormatArray[0], LocaleContextHolder.getLocale()).format(date);
	}

	/**
	 * Convert from string date to Date object. Makes a error message in case of wrong date format.
	 * 
	 * @param stringDate
	 *            string version of the date
	 * @param component
	 *            the associated UIComponent
	 * @return the date object after conversion
	 */
	private Date convertStringToDate(String stringDate, UIComponent component) {

		if (!StringUtils.isBlank(stringDate)) {
			ParseException parseException = null;

			if (StringUtils.containsOnly(stringDate, VALID_CHARACTERS)) {

				String dateFormats = getStringAttribute(component, PATTERN);
				String[] dateFormatArray = getDateFormatArray(dateFormats);
				Date dateD = null;

				/* Checks the different patterns for correct parsing. */
				for (String dateFormat : dateFormatArray) {
					String strippedDateFormat = StringUtils.strip(dateFormat, VALID_DIVIDE_CHARACTERS);
					String strippedStringDate = StringUtils.strip(stringDate, VALID_DIVIDE_CHARACTERS);

					SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getDateInstance();
					sdf.setLenient(false);
					sdf.applyLocalizedPattern(strippedDateFormat);
					try {
						/* Ensures that their is no cut off in the date parsing. */
						if (strippedDateFormat.length() != strippedStringDate.length()) {
							throw new ParseException(dateFormatArray[0], 0);
						}
						dateD = sdf.parse(strippedStringDate);
						parseException = null;
						break;
					} catch (ParseException e) {
						parseException = e;
					}
				}
				if (parseException == null) {
					return dateD;
				}
			} else {
				parseException = new ParseException("'" + VALID_CHARACTERS + "' are the only valid characters.", 0);
			}
			createErrorMessage(parseException, component);
		}
		return null;
	}

	/**
	 * Gets an array of legal date formats.
	 * 
	 * @param dateFormats
	 *            a string representation with date formats splitted with ';'
	 * @return a String array with legal date formats
	 */
	private String[] getDateFormatArray(String dateFormats) {
		Pattern pattern = Pattern.compile(";");
		String[] array = pattern.split(dateFormats);
		if (logger.isDebugEnabled()) {
			String debugString = "Legal date formats: ";
			for (String s : array) {
				debugString += " ['" + s + "'] ";
			}
			logger.debug(debugString);
		}
		return array;
	}

	/**
	 * Gets all of the String attributes.
	 * 
	 * @param component
	 *            the UIComponent that needs conversion
	 * @param value
	 *            the name of the attribute that should be found
	 * @return the attribute that is found
	 */
	private String getStringAttribute(UIComponent component, String value) {
		String attribute = (String) component.getAttributes().get(value);
		if ((attribute == null) || ("".equals(attribute))) {
			throwConverterException(value);
		}
		return attribute;
	}

	/**
	 * Throws a ConverterException that tells the user what's not implemented.
	 * 
	 * @param name
	 *            the name of the missing attribute
	 */
	private void throwConverterException(String name) {
		String errorMsg = "DateConverter: Missing date attribute - add '<f:attribute name='" + name + "' value='<" + name
				+ ">'/>.";
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, errorMsg);
		throw new ConverterException(msg);
	}

	/**
	 * Creates a customized error message based upon the attributes set in the jsf-file.
	 * 
	 * @param parseException
	 *            ParseException
	 * @param component
	 *            the associated UIComponent
	 */
	private void createErrorMessage(ParseException parseException, UIComponent component) {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing failed and a error message will be returned to the user.");
		}

		String fieldName = ((String) component.getAttributes().get(FIELD_NAME) == null) ? "" : (String) component
				.getAttributes().get(FIELD_NAME);
		String errorMsg = getStringAttribute(component, ERROR_MSG);

		String localePattern = getStringAttribute(component, LOCALE_PATTERN);

		if (localePattern == null || "".equalsIgnoreCase(localePattern)) {
			String[] dateFormatArray = getDateFormatArray(getStringAttribute(component, PATTERN));
			localePattern = dateFormatArray[0];
			if (logger.isDebugEnabled()) {
				logger.debug("The localePattern is '" + localePattern
						+ "' and is chosen from the first element in the array of legal date formats.");
			}
		}

		List<String> argMessages = new ArrayList<String>();
		String[] arguments = new String[] { fieldName, localePattern };

		/* Gets all of the arguments from the resource bundle. */
		for (String argument : arguments) {
			if (StringUtils.isBlank(argument)) {
				argMessages.add(argument.toString());
			} else {
				String msg = MessageContextUtil.getMessage(argument.toString(), null);
				argMessages.add(msg);
			}
		}

		String msg = MessageContextUtil.getMessage(errorMsg, argMessages.toArray());

		throw new ConverterException(new FacesMessage(msg, msg), parseException);
	}

}