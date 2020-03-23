package no.stelvio.presentation.jsf.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class NumberConverter could be used to get customized conversion error messages in jsf. This is how you should use this class
 * in your jsf-file (e.g. .xhtml/.jsf/.jsp): <blockquote>
 * 
 * <pre>
 *         	&lt;h:inputText id=&quot;numberId&quot; value=&quot;#{flowScope.number}&quot; &gt; &lt;!-- Normal 
 *         																		JSF input text field --&gt;
 *         		&lt;f:converter converterId=&quot;numberConverter&quot; /&gt; &lt;!-- The numberConverter 
 *         															defined in faces-config --&gt;
 *         		&lt;f:attribute name=&quot;pattern&quot; value=&quot;#,###.##&quot; /&gt; &lt;!-- The number pattern/format. 
 *         					You should use #,##0.00 for decimal format and #,###.## to comply with NAV standardsquot;  --&gt;
 *         		&lt;f:attribute name=&quot;ignoreDecimals&quot; value=&quot;true&quot; /&gt; &lt;!-- If true, 
 *         										ignore decimals and return a Long instead of a Double  --&gt;
 *         		&lt;f:attribute name=&quot;emptyOrNullIsZero&quot; value=&quot;false&quot; /&gt; &lt;!-- If true, 
 *         						interpret null or empty strings as the value 0. Optional, default is false --&gt;
 *         		&lt;f:attribute name=&quot;errorMsg&quot; value=&quot;validation.heltall&quot; /&gt; &lt;!-- 
 *         											Reference to the error message in the resource bundle --&gt;
 *         		&lt;f:attribute name=&quot;fieldName&quot; value=&quot;inntekt.belop&quot; /&gt; &lt;!-- Reference 
 *         												to the {0}-argument for the error message --&gt;
 * 				&lt;f:attribute name=&quot;messageSource&quot; value=&quot;#{messageSource}&quot; /&gt; &lt;!-- 
 * 														OPTIONAL Reference to the resource bundle --&gt;
 *         	&lt;/h:inputText&gt;
 * </pre>
 * 
 * </blockquote>
 * <p>
 * The MessageSource will be retrieved from the application context if not defined.
 * </p>
 * <p>
 * For integers, the return type will be Long, for rational number the return type will be Double.
 * 
 * @see DateConverter
 * 
 * @version $Id$
 */
public class NumberConverter implements Converter {
	private char navDecimalSeparator = ',';

	private char navGroupingSeparator = ' ';

	/** Pattern. */
	String pattern = "pattern";

	/** Error message. */
	String errorMSG = "errorMsg";

	/** Field name. */
	String fieldNAME = "fieldName";

	/** Ignore Decimal. */
	String ignoreDECIMALS = "ignoreDecimals";

	/** Empty or null is zero. */
	String emptyOrNULLisZERO = "emptyOrNullIsZero";

	/** Message source. */
	String messageSOURCE = "messageSource";

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
	 * @param value2convert
	 *            Inncomming value to convert.
	 * @return convert String to Number.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public Object getAsObject(FacesContext context, UIComponent component, String value2convert) throws ConverterException {
		boolean isInteger = false;
		boolean emptyOrNullIsZero = false;
		String value = value2convert;

		if ("true".equalsIgnoreCase(StringUtils.trim(getStringAttribute(component, ignoreDECIMALS, true)))) {
			isInteger = true;
		}

		if ("true".equalsIgnoreCase(StringUtils.trim(getStringAttribute(component, emptyOrNULLisZERO, false)))) {
			emptyOrNullIsZero = true;
		}

		if ("".equals(StringUtils.trim(value)) || value == null) {
			if (emptyOrNullIsZero) {
				value = "0";
			} else {
				return null;
			}
		}
		if (!validateNumberString(value, !isInteger)) {
			createErrorMessage(new ParseException(value, 0), component, context);
		}

		return convertStringToNumber(StringUtils.trim(value), isInteger, component, context);
	}

	/**
	 * Validate that the number string only contains valid characters. If it is a decimal number, also validate that there is
	 * only one occurrence of the decimal separator
	 * 
	 * @param value
	 *            number as string
	 * @param acceptDecimals
	 *            if true, accept comma as decimal separator
	 * @return true if number is valid
	 */
	private boolean validateNumberString(String value, boolean acceptDecimals) {
		boolean isValid = false;
		String acceptedChars = acceptDecimals ? "\\A *-?[0-9" + navDecimalSeparator + " ]+\\Z" : "\\A *-?[0-9 ]+\\Z";
		if (value.matches(acceptedChars)) {
			if (acceptDecimals) {
				if (value.indexOf(navDecimalSeparator) == value.lastIndexOf(navDecimalSeparator)) {
					isValid = true;
				}
			} else {
				isValid = true;
			}
		}

		return isValid;
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
	 * @return convert number to string.
	 * @throws ConverterException
	 *             ConverterException.
	 */
	public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
		return ((value == null) || !(value instanceof Number)) ? "" : convertNumberToString((Number) value, component, context);
	}

	/**
	 * Convert from string number to Number object. Makes a error message in case of wrong number format.
	 * 
	 * @param stringNumber
	 *            string version of the number
	 * @param isInteger
	 *            is true, the returned number will be a Long, otherwise it will be a Double
	 * @param component
	 *            the associated UIComponent
	 * @param context
	 *            FacesContext
	 * @return the date object after conversion
	 */
	private Number convertStringToNumber(String stringNumber, boolean isInteger, UIComponent component, FacesContext context) {
		DecimalFormatSymbols dfs = getNavDecimalFormatSymbols();
		DecimalFormat format = new DecimalFormat(getStringAttribute(component, pattern, true), dfs);

		format.setParseIntegerOnly(isInteger);
		Number ret = null;

		try {
			ret = format.parse(stringNumber);
		} catch (ParseException e) {
			createErrorMessage(e, component, context);
		}

		if (!isInteger && ret instanceof Long) {
			return ret.doubleValue();
		} else if (isInteger && ret instanceof Double) {
			return ret.longValue();
		}
		return ret;
	}

	/**
	 * Get DecimalFormatSymbols matching the NAV spesification (space as 1000 separator and , as decimalseparator).
	 * 
	 * @return the DecimalFormatSymbols
	 */
	private DecimalFormatSymbols getNavDecimalFormatSymbols() {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator(navDecimalSeparator);
		dfs.setGroupingSeparator(navGroupingSeparator);
		return dfs;
	}

	/**
	 * Converts from Number to string.
	 * 
	 * @param number
	 *            the Number to be converted
	 * @param component
	 *            the associated UIComponent
	 * @param context
	 *            FacesContext
	 * @return String version of the date
	 */
	private String convertNumberToString(Number number, UIComponent component, FacesContext context) {
		DecimalFormatSymbols dfs = getNavDecimalFormatSymbols();

		return new DecimalFormat(getStringAttribute(component, pattern, true), dfs).format(number);
	}

	/**
	 * Gets all of the String attributes.
	 * 
	 * @param component
	 *            the UIComponent that needs conversion
	 * @param value
	 *            the name of the attribute that should be found
	 * @param mandatory
	 *            if true, throw a converter exception if attribute is not found. Otherwise, return null or an empty String
	 * @return the attribute that is found
	 */
	private String getStringAttribute(UIComponent component, String value, boolean mandatory) {
		String attribute = (String) component.getAttributes().get(value);
		if (mandatory && ((attribute == null) || ("".equals(attribute)))) {
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
		String errorMsg = "NumberConverter: Missing number attribute - add '<f:attribute name='" + name + "' value='<" + name
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
	 * @param context
	 *            FacesContext
	 */
	private void createErrorMessage(ParseException parseException, UIComponent component, FacesContext context) {
		if (logger.isDebugEnabled()) {
			logger.debug("Parsing failed and a error message will be returned to the user. Context: " + context);
		}

		String errorMsg = getStringAttribute(component, errorMSG, true);
		String fieldName = getStringAttribute(component, fieldNAME, true);

		String[] arguments = new String[] { fieldName };

		/* Retrieves an internationalized message from the messageSource. */
		String message = MessageContextUtil.getMessage(errorMsg, arguments);
		if (logger.isDebugEnabled()) {
			logger.debug("Message to user: \"" + message + "\"");
		}

		/* Sets the error message that is shown in the user interface. */
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);

		throw new ConverterException(msg, parseException);
	}

}