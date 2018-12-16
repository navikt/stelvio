package no.stelvio.presentation.jsf.codestable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.component.html.ext.HtmlSelectOneMenu;
import org.springframework.web.jsf.FacesContextUtils;

import no.stelvio.common.codestable.CodesTableItem;
import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.common.codestable.support.AbstractCodesTableItem;
import no.stelvio.common.codestable.support.AbstractCodesTablePeriodicItem;
import no.stelvio.common.codestable.support.DecodeComparator;
import no.stelvio.common.error.InvalidArgumentException;

/**
 * Custom component for populating dropdown lists with elements from a specific codestable class. The codetable items are
 * retrived from the codes table manager, and mapped into select item elements which are populated into the drop down list. <br>
 * The component is a subcomponent of the JSF (MyFaces Tomahawk) built in drop down list component, and inherits all the
 * attributes provided by the super type component. <br>
 * In addition to the standard attributes, this component supports the following attributes: <br>
 * <ul>
 * <li><code>ctiClass</code> the full class name of the codestableitem class, e.g.
 * <code>no.stelvio.domain.star.example.codestable.HenvendelseTypeCti</code></li>
 * <li><code>defaultValue</code> the default value of the component, which will be selected if no value has been selected</li>
 * <li><code>sorted</code> optional attribute to indicate if the list of elements should be sorted or not, default value is
 * <code>true</code>. This attribute should be set to <code>false</code> if the list of codes table items should not be sorted.</li>
 * <li><code>showCodeValue</code> optional attribute, if true the code value will be shown instead of decode value in drop-down.
 * </li>
 * <li><code>comparator</code> optional attribute, can be used to specify a comparator to use for the sorting of the
 * codestableitems. Default comparator is {@link no.stelvio.common.codestable.support.DecodeComparator}</li>
 * <li><code>showValidOnly</code> optional attribute, can be set to <code>false</code> if one wishes to display codeitems that
 * are invalidated and expired. Defaults to true, displaying only code items that are valid.</li>
 * </ul>
 * <br>
 * Be aware that the CodesTableManager and ExceptionHandlerFacade must be properly configured in the Spring context
 * configuration of the presentation layer in order for this component to function as expected. <br>
 * The faces-config configuration for this component is provided with the framework, and no ' further configuration of this
 * component is required in the application specific faces-context configuration.
 * 
 * CR NAV00163122:  Added functionality that allows filtering based on a date different from the current date.  To filter on 
 * a date, use the setValidOnDate method.  When a Date object is provided through this method, the method 
 * getCodesTableItemsValidForDate will be used in retrieveCodesTableItems to get the Ctpi elements that are valid 
 * on that particular date.  
 * 
 * @author person082681bfb6fd (Capgemini)
 * @author person6045563b8dec
 * @author persone38597605f58 (Capgemini)
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public class CodesTableItemSelectOneMenu extends HtmlSelectOneMenu {

	private final Log log = LogFactory.getLog(this.getClass());

	private static final String ATTRIBUTE_CTI_CLASS = "ctiClass";

	private static final String ATTRIBUTE_DEFAULT_VALUE = "defaultValue";

	private static final String ATTRIBUTE_SORTED = "sorted";

	private static final String ATTRIBUTE_COMPARATOR = "comparator";

	private static final String ATTRIBUTE_SHOW_CODE_VALUE = "showCodeValue";

	private static final String ATTRIBUTE_SHOW_ONLY_VALID_CODEITEMS = "showValidOnly";
	
	private static final String ATTRIBUTE_VALID_ON_DATE_CODEITEMS = "validOnDate";
	
	private String ctiClass;

	private String comparator;

	private String defaultValue;

	private boolean sorted = true;

	private boolean showCodeValue = false;

	private boolean showValidOnly = true;

	private Date validOnDate = null;
	
	private CodesTableManager codesTableManager;

	/**
	 * Encodes this component, the codestableitems which should be displayed in the dropdown list are populated as SelectItems,
	 * and are added as children of this component.
	 * 
	 * @param facesContext
	 *            the current facesContext instance
	 * @throws IOException
	 *             IOException.
	 */
	@Override
	public void encodeBegin(FacesContext facesContext) throws IOException {
		buildSelectItems();
		super.encodeBegin(facesContext);
	}

	/**
	 * Saves the state of this component.
	 * 
	 * @param context
	 *            The current FacesContext intstance
	 * @return <ul>
	 *         <li>values[0] = super.saveState(context);</li>
	 *         <li>values[1] = getCtiClass();</li>
	 *         <li>values[2] = getDefaultValue();</li>
	 *         <li>values[3] = isSorted();</li>
	 *         <li>values[4] = getComparator();</li>
	 *         <li>values[5] = isShowCodeValue();</li>
	 *         </ul>
	 */
	@Override
	public Object saveState(FacesContext context) {
		Object[] values = new Object[7];
		values[0] = super.saveState(context);
		values[1] = getCtiClass();
		values[2] = getDefaultValue();
		values[3] = isSorted();
		values[4] = getComparator();
		values[5] = isShowCodeValue();
		values[6] = getValidOnDate();
		return values;
	}

	/**
	 * Restores the state of this component.
	 * 
	 * @see javax.faces.context.FacesContext
	 * @param context
	 *            FacesContext.
	 * @param state
	 *            Object.
	 */
	@Override
	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;

		super.restoreState(context, values[0]);
		setCtiClass((String) values[1]);
		setDefaultValue((String) values[2]);
		setSorted((Boolean) values[3]);
		setComparator((String) values[4]);
		setShowCodeValue((Boolean) values[5]);
		setValidOnDate((Date) values[6]);
	}

	/**
	 * Utility method for sorting the codestable items with the comparator specified or the default comparator if none is
	 * specified. If the <code>sorted</code> attribute is set to false, no sorting is performed.
	 * 
	 * @param codesTableItems
	 *            the set of codestableitems to sort
	 * @return the sorted set of codestableitems
	 */
	private Set<? extends AbstractCodesTableItem> sortItems(Set<? extends AbstractCodesTableItem> codesTableItems) {
		if (!isSorted()) {
			return codesTableItems;
		}
		Comparator sortComparator = resolveComparator();
		SortedSet<AbstractCodesTableItem> sorted = new TreeSet<AbstractCodesTableItem>(sortComparator);
		sorted.addAll(codesTableItems);

		return sorted;
	}

	/**
	 * Resolves the comparator to use for the sorting of the elements. The comparator to use is specified as a
	 * <code>String</code> literal and must be resolved to a class and instanciated as an object. If the comparator can not be
	 * created, the default comparator is used.
	 * 
	 * @return the comparator to use
	 */
	private Comparator resolveComparator() {
		if (getComparator() == null) {
			return new DecodeComparator();
		}
		Class comparatorClass = resolveClass(getComparator(), ATTRIBUTE_COMPARATOR);
		Comparator comparator;
		try {
			comparator = (Comparator) comparatorClass.newInstance();
		} catch (IllegalAccessException | InstantiationException e) {
			log.info("Error creating comparator for class " + getComparator());
			comparator = new DecodeComparator();
		}
		return comparator;
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
			throw new InvalidArgumentException(className, cnfe);
		}

		return clazz;
	}

	/**
	 * Retrieves the codestableitems from the codestablemanager and mappes these into SelectItems which are displayed in the
	 * drop down list. The select items are added as children of this component.
	 */
	private void buildSelectItems() {
		if (this.getChildCount() > 0) {
			return;
		}

		Set<? extends AbstractCodesTableItem> codesTableItems = sortItems(retrieveCodesTableItems());

		List<SelectItem> selectItems = new ArrayList<>();

		if (getDefaultValue() != null && !"".equals(getDefaultValue())) {
			SelectItem item = new SelectItem("", getDefaultValue());
			selectItems.add(item);
		}

		if (!isDisplayValueOnly()) {
			for (AbstractCodesTableItem cti : codesTableItems) {
				if (cti.isValid()) {
					selectItems.add(createSelectItem(cti));
				}
			}
		} else {
			for (AbstractCodesTableItem cti : codesTableItems) {
				selectItems.add(createSelectItem(cti));
			}
		}

		UISelectItems uiSelectItems = new UISelectItems();
		uiSelectItems.setValue(selectItems);
		getChildren().add(uiSelectItems);
	}

	private SelectItem createSelectItem(AbstractCodesTableItem cti)
	{
		SelectItem item = new SelectItem();
		item.setDescription(cti.getDecode().toString());
		item.setValue(cti.getCodeAsString());
		if (isShowCodeValue()) {
			item.setLabel(cti.getCodeAsString());
		} else {
			item.setLabel(cti.getDecode().toString());
		}
		return item;
	}

	/**
	 * Retrives the codes table items from the codestable manager. Fetches only codeitems valid today, unless the
	 * "showValidOnly" atttribute is set to false. Default is true for this attribute. As of CR NAV00163122, this method will
	 * return codeitems valid at a given date if a date is provided.
	 * 
	 * @return koder
	 */
	private Set<? extends AbstractCodesTableItem> retrieveCodesTableItems() {
		Set<? extends CodesTableItem> koder;
		if (AbstractCodesTablePeriodicItem.class.isAssignableFrom(resolveCtiClass())) {

			if (getValidOnDate() != null && !isDisplayValueOnly()) {
				koder = getCodesTableManager().getCodesTablePeriodic(resolveCtiClass()).getCodesTableItemsValidForDate(
						getValidOnDate());
			} else if (isShowValidOnly() && !isDisplayValueOnly()) {
				koder = getCodesTableManager().getCodesTablePeriodic(resolveCtiClass()).getCodesTableItemsValidToday();
			} else {
				koder = getCodesTableManager().getCodesTablePeriodic(resolveCtiClass()).getCodesTableItems();
			}
		} else {
			koder = getCodesTableManager().getCodesTable(resolveCtiClass()).getCodesTableItems();
		}
		return koder;
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
	 * @param ctiClass
	 *            the ctiClass to set
	 */
	public void setCtiClass(String ctiClass) {
		this.ctiClass = ctiClass;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		if (getValueExpression(ATTRIBUTE_DEFAULT_VALUE) != null) {
			defaultValue = (String) getValueExpression(ATTRIBUTE_DEFAULT_VALUE).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
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
	 * 
	 * @param codesTableManager
	 *            the codesTableManager to set
	 */
	public void setCodesTableManager(CodesTableManager codesTableManager) {
		this.codesTableManager = codesTableManager;
	}

	/**
	 * 
	 * @return the comparator attribute
	 */
	public String getComparator() {
		if (getValueExpression(ATTRIBUTE_COMPARATOR) != null) {
			comparator = (String) getValueExpression(ATTRIBUTE_COMPARATOR).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}

		return comparator;
	}

	/**
	 * 
	 * @param comparator
	 *            the comparator to set
	 */
	public void setComparator(String comparator) {
		this.comparator = comparator;
	}

	/**
	 * @return the sorted attribute
	 */
	public boolean isSorted() {
		if (getValueExpression(ATTRIBUTE_SORTED) != null) {
			sorted = (Boolean) getValueExpression(ATTRIBUTE_SORTED).getValue(FacesContext.getCurrentInstance().getELContext());
		}

		return sorted;
	}

	/**
	 * 
	 * @param sorted
	 *            the sorted to set
	 */
	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	/**
	 * @return the showCodeValue
	 */
	public boolean isShowCodeValue() {
		if (getValueExpression(ATTRIBUTE_SHOW_CODE_VALUE) != null) {
			showCodeValue = (Boolean) getValueExpression(ATTRIBUTE_SHOW_CODE_VALUE).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}

		return showCodeValue;
	}

	/**
	 * @param showCodeValue
	 *            the showCodeValue to set
	 */
	public void setShowCodeValue(boolean showCodeValue) {
		this.showCodeValue = showCodeValue;
	}

	/**
	 * Returns true if one should only display code values which are fetched using getCodestableItemsValidToday. False if all
	 * values, also invalidated and expired values, should be displayed.
	 * 
	 * @return
	 */
	public boolean isShowValidOnly() {
		if (getValueExpression(ATTRIBUTE_SHOW_ONLY_VALID_CODEITEMS) != null) {
			showValidOnly = (Boolean) getValueExpression(ATTRIBUTE_SHOW_ONLY_VALID_CODEITEMS).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}
		return showValidOnly;
	}

	public void setShowValidOnly(boolean showValidOnly) {
		this.showValidOnly = showValidOnly;
	}
	
	/**
	 * Returns the date that is used for filtering the codeitems
	 * 
	 * @return
	 */
	public Date getValidOnDate() {
		if (getValueExpression(ATTRIBUTE_VALID_ON_DATE_CODEITEMS) != null) {
			validOnDate = (Date) getValueExpression(ATTRIBUTE_VALID_ON_DATE_CODEITEMS).getValue(
					FacesContext.getCurrentInstance().getELContext());
		}
		return validOnDate;
	}
	
	/**
	 *  @param validOnDate
	 *  If codeitems should be filtered on a date other than today, validOnDate must be provided
	 *
	 */
	public void setValidOnDate(Date validOnDate) {
		this.validOnDate = validOnDate;
	}

	
}