package no.stelvio.presentation.jsf.validator;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.common.error.InvalidArgumentException;
import no.stelvio.presentation.binding.context.MessageContextUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * 
 * @author person37c6059e407e (Capgemini)
 * @version $Id$
 * 
 */
public class CodesTableItemExistValidator extends AbstractFieldNameValidator {
	private final Log log = LogFactory.getLog(this.getClass());

	private static final String DEFAULT_CTI_CLASS = "no.nav.domain.pensjon.kjerne.kodetabeller.PoststedCti";

	private CodesTableManager codesTableManager;

	private String ctiClass;

	private static final String ATTRIBUTE_CTI_CLASS = "ctiClass";

	/** Invalid format key. */
	public static final String INVALID_FORMAT = "no.stelvio.presentation.validator.CodesTableItemExistValidator.INVALID_FORMAT";

	/** Invalid format key. */
	public static final String INVALID_POSTCODE = 
			"no.stelvio.presentation.validator.CodesTableItemExistValidator.INVALID_POSTCODE";

	private int length = 4;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateField(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if (value != null) {
			String postCode = String.valueOf(value);

			/** Check the postCode is 4 or higher digit */
			if (Pattern.compile("[0-9]{" + length + "}").matcher(postCode).matches()) {
				/** Get codeTableItems from codeTableManager */
				Set<? extends AbstractCodesTableItem> codesTableItems = retrieveCodesTableItems();
				for (AbstractCodesTableItem cti : codesTableItems) {
					if (cti.getCodeAsString().equals(postCode)) {
						/** Find code and return */
						return;
					}
				}

				/** Can not find the valid value from codeTableItem */
				Object[] args = { getFieldName() };
				throw new ValidatorException(new FacesMessage(MessageContextUtil.getMessage(INVALID_POSTCODE, args)));
			} else {
				Object[] args = { getFieldName() };
				throw new ValidatorException(new FacesMessage(MessageContextUtil.getMessage(INVALID_FORMAT, args)));
			}
		}
	}

	/**
	 * Retrives the codes table items from the codestable manager.
	 * 
	 * @return Codetable codes.
	 */
	@SuppressWarnings("unchecked")
	private Set<? extends AbstractCodesTableItem> retrieveCodesTableItems() {
		Set<? extends CodesTableItem> koder = null;
		if (AbstractCodesTablePeriodicItem.class.isAssignableFrom(resolveCtiClass())) {
			koder = getCodesTableManager().getCodesTablePeriodic(resolveCtiClass()).getCodesTableItems();
		} else {
			koder = getCodesTableManager().getCodesTable(resolveCtiClass()).getCodesTableItems();
		}
		return koder;
	}

	/**
	 * Private helper method for resolving class objects from String literals specified as attributes of the component.
	 * 
	 * @param className
	 *            the class to resolve
	 * @param attribute
	 *            the name of the attribute, if the class cannot be resolved this is used in the exception thrown to inform the
	 *            developer of the error that has occured.
	 * @return the Class object of the classname specified as an attribute of this component.
	 */
	private Class resolveClass(String className, String attribute) {
		Class clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			throw new InvalidArgumentException("ClassNotFoundException for " + className + " with attribute " + attribute);
		}

		return clazz;
	}

	/**
	 * Resolves the ctiClass to use for this component.
	 * 
	 * @return the Class of the ctiClass to use
	 */
	private Class resolveCtiClass() {
		return resolveClass(getCtiClass(), ATTRIBUTE_CTI_CLASS);
	}

	/**
	 * Utilty method for retriving the first bean in a map Used for internal use only because of the lack of dependency
	 * injection support in JSF components.
	 * 
	 * @param beanMap
	 *            the map of beans
	 * @return the first bean in the map
	 */
	private Object getFirstBean(Map beanMap) {
		Object bean = null;
		if (beanMap != null && beanMap.size() > 0) {
			Iterator i = beanMap.keySet().iterator();
			bean = beanMap.get(i.next());
		}
		return bean;
	}

	/**
	 * Get codestable item class.
	 * 
	 * @return the ctiClass
	 */
	public String getCtiClass() {
		if (ctiClass == null) {
			ctiClass = DEFAULT_CTI_CLASS;
		}
		return this.ctiClass;
	}

	/**
	 * Set codestable item class.
	 * 
	 * @param ctiClass
	 *            the ctiClass to set
	 */
	public void setCtiClass(String ctiClass) {
		this.ctiClass = ctiClass;
	}

	/**
	 * Get codestable manager.
	 * 
	 * @return codesTableManager to use
	 */
	public CodesTableManager getCodesTableManager() {
		if (codesTableManager == null) {
			Map beanMap = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBeansOfType(
					CodesTableManager.class);
			codesTableManager = (CodesTableManager) getFirstBean(beanMap);

			if (log.isInfoEnabled()) {
				if (codesTableManager == null) {
					log.info("No CodesTableManager found in the application context");
				}
			}
		}
		return codesTableManager;
	}

	/**
	 * Set codestable manager.
	 * 
	 * @param codesTableManager
	 *            the codesTableManager to set
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}

	/**
	 * Get length.
	 * 
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Set length.
	 * 
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

}
