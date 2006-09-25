package no.stelvio.web.framework.validator;

import no.stelvio.common.framework.util.DateUtil;
import no.stelvio.common.framework.util.FNRUtil;
import no.stelvio.common.framework.util.StringHelper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * This class contains the default validations that are used in the validator-rules.xml file.
 * <p/>
 * In general passing in a null or blank will return a null Object or a false boolean. However, nulls and blanks do not
 * result in an error being added to the errors.
 *
 * @author person3f0dd77ee0b7 Holgeid, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: FieldChecks.java 2863 2006-04-25 11:17:41Z psa2920 $
 */
public final class FieldChecks {

	/** Commons Logging instance. */
	private static final Log LOG = LogFactory.getLog(FieldChecks.class);

	public static final String FIELD_TEST_NULL = "NULL";
	public static final String FIELD_TEST_NOTNULL = "NOTNULL";
	public static final String FIELD_TEST_EQUAL = "EQUAL";
	private static final int FNR_LENGTH = 11;

	/** Private constructor to avoid instantiations. */
	private FieldChecks() {
	}

	/**
	 * Checks if the field is a 'fødselsnummer' or empty.
	 *
	 * @param bean The bean validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @return true if meets stated requirements, false otherwise.
	 */
	public static boolean validateFnr(Object bean,
	                                     ValidatorAction va,
	                                     Field field,
	                                     ActionErrors errors,
	                                     HttpServletRequest request) {
		// hent ut fnr
		String value = StringUtils.deleteWhitespace(retrieveValue(bean, field.getProperty()));

		int result = validateFnrLength(value);
		if(result == 1){
			// 1 == lenght is 0 (blank) -> OK
			return true;	
		} else if(result == 2){
			// 2 == length is not 11
			errors.add(field.getKey(), new ActionError("Roller.ikke.gyldig.fnr", value));
			return false;				
		}

		// juster måned hvis vi har et DNR
		String valueAdjusted = makeDnrAdjustments(value);

		result = validateFnrDetails(value, valueAdjusted);
		if(result == 1 || result == 2){
			// 1 == feil i modulus 11 sjekk
			// 2 == feil i dato sjekk
			errors.add(field.getKey(), new ActionError("Roller.ikke.gyldig.fnr", value));
			return false;	
		} 
		
		// alt er ok
		return true;
	}

	/**
	 * Checks if the field is a 'fødselsnummer' or empty.
	 *
	 * @param value fødselsnummer.
	 * @return 0 if meets stated requirements, 1 if blank (ok), 2 if lenght <> 11.
	 */
	public static int validateFnrLength(String value) {

		// sjekk om fnr er tom/blank
		if (StringUtils.isBlank(value)) {
			// ikke add ny error, da blankt felt er OK
			return 1;
		}
		// sjekk om lengde == 11
		if (value.length() < FNR_LENGTH || value.length() > FNR_LENGTH ) {
			return 2;
		}
	
		// ingen feil er funnet
		return 0;		
	}

	/**
	 * Checks if the field is a 'fødselsnummer' or empty.
	 *
	 * @param value fødselsnummer.
	 * @param valueAdjusted fødselsnummer korrigert. for Dnr eller BOST nr.
	 * @return 0 if meets stated requirements, 1 if length error, 2 if mod11 error, 3 if date error.
	 */
	public static int validateFnrDetails(String value, String valueAdjusted) {

		// sjekk om fnr er modulus 11 kompatibelt								  			
		if(!FNRUtil.isMod11Compliant(value)){
			return 1;
		}

		// sjekk at fnr dato er gyldig
		if(!FNRUtil.isFnrDateValid(valueAdjusted)){
			return 2;
		}
	
		// ingen feil er funnet
		return 0;		
	}

	/**
	 * Korrigerer fnr for evt. D-nummer verdi før det skal valideres
	 *
	 * @param value Fnr eller Dnr
	 * @return riktig eller korrigert fnr
	 */
	private static String makeDnrAdjustments(String value){
		
		if(StringUtils.isBlank(value)){
			return value;
		}

		// fnr vil vare på formatet <DDMMAAXXXYY>
		int day = Integer.parseInt(value.substring(0, 2));

		// DNR omregning
		if (day > 40 && day <= 71) {
			day -= 40;

			StringBuffer fnr = new StringBuffer(value);
			return fnr.replace(0, 2, StringHelper.insertLeadingZeros(new Integer(day).toString(), 2)).toString();
		}

		return value;
	}
		
	/**
	 * Checks if the field is a date with the day being the first in a month. Also allows an empty date.
	 *
	 * @param bean The bean validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @return true if meets stated requirements, false otherwise.
	 */
	public static boolean validateFirstDayOfMonth(Object bean,
	                                                 ValidatorAction va,
	                                                 Field field,
	                                                 ActionErrors errors,
	                                                 HttpServletRequest request) {
		return validateDayOfMonthCommon(bean, va, field, errors, request, "first", new Predicate() {
			public boolean evaluate(Object date) {
				return DateUtil.isFirstDayOfMonth((Date) date);
			}
		});
	}

	/**
	 * Checks if the field is a date with the day being the last in a month. Also allows an empty date.
	 *
	 * @param bean The bean validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @return true if meets stated requirements, false otherwise.
	 */
	public static boolean validateLastDayOfMonth(Object bean,
	                                                ValidatorAction va,
	                                                Field field,
	                                                ActionErrors errors,
	                                                HttpServletRequest request) {
		return validateDayOfMonthCommon(bean, va, field, errors, request, "last", new Predicate() {
			public boolean evaluate(Object date) {
				return DateUtil.isLastDayOfMonth((Date) date);
			}
		});
	}

	/**
	 * Checks if the field is a valid input date
	 *
	 * @param bean The bean validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @return true, eller false dersom dette ikke er en gyldig dato.
	 */
	public static boolean validateInputDate(Object bean,
	                                        ValidatorAction va,
	                                        Field field,
	                                        ActionErrors errors,
	                                        HttpServletRequest request) {
		String value = retrieveValue(bean, field.getProperty());
		boolean validatedOk = validateInputDateCommon(value, va, field, errors, request);

		if (LOG.isDebugEnabled()) {
			LOG.debug("Is '" + value + "' as an input date for field " + field.getProperty() + " a valid value: " + validatedOk);
		}

		return validatedOk;
	}

	/**
	 * Evaluates an expression written in EL (Expression Language as used in JSP), erroring out if it evaluates to false.
	 * <p>
	 * The following variables can be used inside the expression:
	 * <ul>
	 *  <li>this - the object which is validated (called 'this' from now on)
	 *  <li>value - 'this' as a string
	 *  <li>form - the form 'this' is a part of
	 *  <li>indexedProperty - if 'this' is a property on an indexed property of the form, it is set to the current object of
	 *                        the indexed property
	 *  <li>field - holds all information set in the configuration of this validation (typically in validation.xml)
	 * </ul>
	 * 
	 * @param bean the object.
	 * @param va the validator action.
	 * @param field the field.
	 * @param errors the action errors.
	 * @param validator the validator.
	 * @param request current HTTP request.
	 * @return true if expressions validates to true, false otherwise.
	 */
	public static boolean validateEl(Object bean,
	                                 ValidatorAction va,
	                                 Field field,
	                                 ActionErrors errors,
	                                 Validator validator,
	                                 HttpServletRequest request) {

// TODO: Should not use this
//		PageContext pageContext = new FakePageContext(request);
//		String test = field.getVarValue("test");

//		if (field.isIndexed()) {
//			pageContext.setAttribute("indexedProperty", bean);
//			pageContext.setAttribute("form", validator.getParameterValue(Validator.BEAN_PARAM));
//		} else {
//			pageContext.setAttribute("form", bean);
//		}

//		pageContext.setAttribute("field", field);
//		pageContext.setAttribute("this", retrieveProperty(bean, field.getProperty()));
//		pageContext.setAttribute("value", ValidatorUtils.getValueAsString(bean, field.getProperty()));
		Boolean result = Boolean.FALSE;

//		if (LOG.isDebugEnabled()) {
//			LOG.debug("The EL expression to test: " + test);
//			LOG.debug("PageContext to use for validating the EL expression: " + pageContext);
//		}

//		try {
//			result = (Boolean) ExpressionEvaluatorManager.evaluate("validateEl", test, Boolean.class, pageContext);
//		} catch (JspException je) {
//			if (LOG.isWarnEnabled()) {
//				LOG.warn(je.getMessage(), je);
//			}
//		}

		if (!result.booleanValue()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Validation failed");
			}

			errors.add(field.getKey(), Resources.getActionError(request, va, field));
		}

		return result.booleanValue();
	}

	/**
	 * Helper method for doing day of month validations.
	 *
	 * @param bean The bean validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @param type specifies the type of day of month validation.
	 * @param predicate holds the specific validaton to perform.
	 * @return true if meets stated requirements, false otherwise.
	 */
	private static boolean validateDayOfMonthCommon(final Object bean,
	                                                final ValidatorAction va,
	                                                final Field field,
	                                                final ActionErrors errors,
	                                                final HttpServletRequest request,
	                                                final String type,
	                                                final Predicate predicate) {
		String value = retrieveValue(bean, field.getProperty());
		boolean validatedOk = true;

		if (validateInputDateCommon(value, va, field, errors, request)) {
			Date date = DateUtil.parseInputString(value, true);

			// Allow an empty string in value which will become null
			if (null != date) {
				boolean lastDayOfMonth = predicate.evaluate(date);

				if (!lastDayOfMonth) {
					errors.add(field.getKey(), Resources.getActionError(request, va, field));
					validatedOk = false;
				}
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Is '" + value + "' in field " + field.getProperty()
			          + " the " + type + " day of month: " + validatedOk);
		}

		return validatedOk;
	}

	/**
	 * Helper method for validating an input date.
	 *
	 * @param value The string validation is being performed on.
	 * @param va The <code>ValidatorAction</code> that is currently being performed.
	 * @param field The <code>Field</code> object associated with the current field being validated.
	 * @param errors The <code>ActionErrors</code> object to add errors to if any validation errors occur.
	 * @param request Current request object.
	 * @return true, eller false dersom dette ikke er en gyldig dato.
	 */
	private static boolean validateInputDateCommon(final String value,
	                                                  final ValidatorAction va,
	                                                  final Field field,
	                                                  final ActionErrors errors,
	                                                  final HttpServletRequest request
	) {
		boolean validatedOk = true;

		try {
			DateUtil.parseInputString(value, true);
		} catch (IllegalArgumentException ia) {
			errors.add(field.getKey(), Resources.getActionError(request, va, field));
			validatedOk = false;
		}

		return validatedOk;
	}

	/**
	 * Helper method for retrieving a property from a bean.
	 *
	 * @param bean the bean.
	 * @param field the field.
	 * @return the property.
	 */
	private static Object retrieveProperty(Object bean, String field) {
		Object property = null;

		try {
			property = PropertyUtils.getProperty(bean, field);
		} catch (IllegalAccessException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(e.getMessage(), e);
			}
		} catch (InvocationTargetException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(e.getMessage(), e);
			}
		} catch (NoSuchMethodException e) {
			if (LOG.isDebugEnabled()) {
				LOG.debug(e.getMessage(), e);
			}
		}

		return property;
	}

	/**
	 * Retrieves the value of the bean, either the bean itself, if it is a <code>String</code>, otherwise use the value of
	 * the beans property specified by propertyName.
	 *
	 * @param bean bean to get value from.
	 * @param propertyName name of the property of the bean if bean is not <code>String</code>.
	 * @return the value of the bean.
	 */
	private static String retrieveValue(final Object bean, final String propertyName) {
		String value;

		if (isString(bean)) {
			value = (String) bean;
		} else {
			value = ValidatorUtils.getValueAsString(bean, propertyName);
		}

		return value;
	}


	/**
	 * Return <code>true</code> if the specified object is a String or a <code>null</code> value.
	 *
	 * @param o Object to be tested
	 * @return The string value
	 */
	static boolean isString(Object o) {
		return null == o || String.class.isInstance(o);
	}
}
