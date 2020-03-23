package no.stelvio.presentation.jsf.codestable;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.jsf.FacesContextUtils;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.common.error.InvalidArgumentException;

/**
 * Custom component for translating a code value belong to a CodesTableItem to the correct decode value.The codetable items are
 * retrived from the codes table manager. <br>
 * The component is a subcomponent of the JSF built in {@link HtmlOutputText}, and inherits all the attributes provided by the
 * super type component. <br>
 * In addition to the standard attributes, this component supports the following attributes:
 * <ul>
 * <li><code>ctiClass</code> the full class name of the codestableitem class, e.g.
 * <code>no.nav.domain.pensjon.kjerne.kodetabeller.KravGjelderCode</code></li>
 * <li><code>codeValue</code> the code value of the component, which will translated into a decode value. The decode value will
 * be set as the value on the super class.</li>
 * </ul>
 * 
 * Be aware that the CodesTableManager must be properly configured in the Spring context configuration of the presentation layer
 * in order for this component to function as expected. <br>
 * The faces-config configuration for this component is provided with the framework, and no further configuration of this
 * component is required in the application specific faces-context configuration.
 * 
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class CodesTableItemOutputText extends HtmlOutputText {
	private static final Log LOGGER = LogFactory.getLog(CodesTableItemOutputText.class);

	private static final String ATTRIBUTE_CTI_CLASS = "ctiClass";

	private static final String ATTRIBUTE_CODE_VALUE = "codeValue";

	private String ctiClass;

	private String codeValue;

	private CodesTableManager codesTableManager;

	/**
	 * Encodes this component, the correct codestableitem is found and the decode value is set as value.
	 * 
	 * @param facesContext
	 *            the current facesContext instance
	 */
	@Override
	public void encodeBegin(FacesContext facesContext) {
		Set<? extends AbstractCodesTableItem> codesTableItems = retrieveCodesTableItems();

		AbstractCodesTableItem chosenCti = null;
		for (AbstractCodesTableItem cti : codesTableItems) {
			if (codeValue.equalsIgnoreCase(cti.getCodeAsString())) {
				chosenCti = cti;
				break;
			}
		}
		if (chosenCti == null) {
			throw new InvalidArgumentException(codeValue);
		}

		this.setValue(chosenCti.getDecode());
	}

	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = super.saveState(context);
		values[1] = getCtiClass();
		values[2] = getCodeValue();
		return values;
	}

	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		super.restoreState(context, values[0]);
		setCtiClass((String) values[1]);
		setCodeValue((String) values[2]);
	}

	/**
	 * Retrives the codes table items from the codestable manager.
	 * 
	 * @return a set of CodesTableItems
	 */
	private Set<? extends AbstractCodesTableItem> retrieveCodesTableItems() {
		Set<? extends CodesTableItem> ctiSet;
		if (AbstractCodesTablePeriodicItem.class.isAssignableFrom(resolveCtiClass())) {
			ctiSet = getCodesTableManager().getCodesTablePeriodic(resolveCtiClass()).getCodesTableItems();
		} else {
			ctiSet = getCodesTableManager().getCodesTable(resolveCtiClass()).getCodesTableItems();
		}
		return ctiSet;
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
		Class clazz;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			throw new InvalidArgumentException(attribute);
		}
		return clazz;
	}

	/**
	 * Return the CodesTableManager from the Spring configuration of the presentation layer.
	 * 
	 * @return codesTableManager to use
	 */
	protected CodesTableManager getCodesTableManager() {
		if (codesTableManager == null) {
			Map beanMap = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBeansOfType(
					CodesTableManager.class);
			codesTableManager = (CodesTableManager) getFirstBean(beanMap);

			if (LOGGER.isInfoEnabled()) {
				if (codesTableManager == null) {
					LOGGER.info("No CodesTableManager found in the application context");
				}
			}
		}
		return codesTableManager;
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
	 * Returns the ctiClass.
	 * 
	 * @return the ctiClass
	 */
	public String getCtiClass() {
		if (getValueExpression(ATTRIBUTE_CTI_CLASS) != null) {
			ctiClass = (String) getValueExpression(ATTRIBUTE_CTI_CLASS).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}

		return ctiClass;
	}

	/**
	 * Sets the ctiClass.
	 * 
	 * @param ctiClass
	 *            the ctiClass to set
	 */
	public void setCtiClass(String ctiClass) {
		this.ctiClass = ctiClass;
	}

	/**
	 * Returns the codeValue.
	 * 
	 * @return the codeValue
	 */
	public String getCodeValue() {
		if (getValueExpression(ATTRIBUTE_CODE_VALUE) != null) {
			codeValue = (String) getValueExpression(ATTRIBUTE_CODE_VALUE).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}
		return codeValue;
	}

	/**
	 * Sets the codeValue.
	 * 
	 * @param codeValue
	 *            the codeValue to set
	 */
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}

}