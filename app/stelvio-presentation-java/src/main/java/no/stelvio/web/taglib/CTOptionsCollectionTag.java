package no.stelvio.web.taglib;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.codestable.CodesTableManager;
import no.stelvio.web.taglib.support.CtTagSupport;
import no.stelvio.web.taglib.support.ExpressionEvaluator;


/**
 * Tag for creating an option collection based on a codes table. 
 * The codes table is specified with the name of the class 
 * (for example: 'KodeBrevDO') in the codestable property of the tag.
 * 
 * <p/>
 * 
 * The collection may be an array of objects, a Collection, Enumeration,
 * Iterator, or Map.
 * 
 * <p/>
 * 
 * <b>NOTE</b> - This tag requires a Java2 (JDK 1.2 or later) platform.
 *
 * @author person02f3de2754b4, Accenture
 * @author personf8e9850ed756, Accenture
 * @version $Id: CTOptionsCollectionTag.java 2854 2006-04-25 10:56:51Z psa2920 $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class CTOptionsCollectionTag extends TagSupport {

	private static final String BEAN_VALUE_PROPERTY = "kode";
	private static final String BEAN_LABEL_PROPERTY = "dekode";
//	protected static final MessageResources MESSAGES =
//		MessageResources.getMessageResources(Constants.Package + ".LocalStrings");

	// ------------------------------------------------------------- Properties

	private final Log log = LogFactory.getLog(CTOptionsCollectionTag.class);

	/** Whether an empty option should be added to the drop down list. */
	private boolean addEmptyOption = false;

	/** Whether an empty option should be added to the drop down list (as a potential EL expression). */
	private String elAddEmptyOption;

	/**The text to show for the empty option if enabled. */
	private String emptyOptionText;

	/** The text to show for the empty option if enabled (as a potential EL expression). */
	private String elEmptyOptionText;

	/** The class name of the codes table to show. */
	private String codestable = null;

	/** The class name of the codes table to show as a potential EL expression. */
	private String elCodestable = null;

	/**
	 * Holds a bean which implements {@link CodesTableManager.Filter}. If not null, it will be used to select which
	 * elements of the codes table that should be shown.
	 */
//	private CodesTableManager.Filter ctFilter = null; TODO: new way of handling this

	/**
	 * A potential EL expression that points to a bean which implements {@link CodesTableManager.Filter}.
	 *
	 * @see #ctFilter
	 */
	private String elFilterBean = null;

	/**
	 * A potential EL expression that points to the property of the surrounding form bean that returns the implementation of
	 * {@link CodesTableManager.Filter} to be used.
	 *
	 * @see #ctFilter
	 */
	private String elFilterFormProperty = null;

	/**
	 * Holds a bean which implements {@link Comparator}. If not null, it will be used to sort the elements in the
	 * codes table.
	 */
	private Comparator comparator = null;

	/**
	 * A potential EL expression that points to a bean which implements {@link Comparator}.
	 *
	 * @see #comparator
	 */
	private String elComparatorBean = null;

	/**
	 * A potential EL expression that points to the property of the surrounding form bean that returns the implementation of
	 * {@link Comparator} to be used.
	 *
	 * @see #comparator
	 */
	private String elComparatorFormProperty = null;

	/** Should the label values be filtered for HTML sensitive characters? */
	private boolean filterHtml = true;

	/** Should the label values be filtered for HTML sensitive characters (as a potential EL expression)? */
	private String elFilterHtml = null;

	/** The style associated with this tag. */
	private String style = null;

	/** The style associated with this tag as a potential EL expression. */
	private String elStyle = null;

	/** The named style class associated with this tag. */
	private String styleClass = null;

	/** The named style class associated with this tag as a potential EL expression. */
	private String elStyleClass = null;

	/** If the property set as selected in the parent Select tag should be included in the filtered collection. */
	private boolean addProperty = false;

	/**
	 * If the property set as selected in the parent Select tag should be included in the filtered collection (as a potential EL
	 * expression).
	 */
	private String elAddProperty = null;

	/**
	 * Sets if an empty option should be added or not. 
	 * 
	 * @param addEmptyOption true or false.
	 */
	public void setAddEmptyOption(String addEmptyOption) {
		this.elAddEmptyOption = addEmptyOption;
	}

	/**
	 * Sets the empty option text.
	 * 
	 * @param emptyOptionText text.
	 */
	public void setEmptyOptionText(String emptyOptionText) {
		this.elEmptyOptionText = emptyOptionText;
	}

	/**
	 * Sets the name of the codes table.
	 * 
	 * @param codestable the codes table name.
	 */
	public void setCodestable(final String codestable) {
		this.elCodestable = codestable;
	}

	/**
	 * Sets the name of a filter bean to be used.
	 * 
	 * @param filterBean the bean name.
	 */
	public void setFilterBean(final String filterBean) {
		this.elFilterBean = filterBean;
	}

	/**
	 * Sets the filter form property.
	 * 
	 * @param filterFormProperty the form property.
	 */
	public void setFilterFormProperty(final String filterFormProperty) {
		this.elFilterFormProperty = filterFormProperty;
	}

	/**
	 * Sets the comparator form property.
	 *
	 * @param comparatorFormProperty the form property.
	 */
	public void setComparatorFormProperty(final String comparatorFormProperty) {
		this.elComparatorFormProperty = comparatorFormProperty;
	}

	/**
	 * Sets the filter html property.
	 * 
	 * @param filter the filter html property.
	 */
	public void setFilterHtml(final String filter) {
		this.elFilterHtml = filter;
	}

	/**
	 * Sets the style associated with this tag as a potential EL expression.
	 * 
	 * @param style the style.
	 */
	public void setStyle(final String style) {
		this.elStyle = style;
	}

	/**
	 * Sets the named style class associated with this tag as a potential EL expression.
	 * 
	 * @param styleClass the style class.
	 */
	public void setStyleClass(final String styleClass) {
		this.elStyleClass = styleClass;
	}

	/**
	 * Sets if the property set as selected in the parent Select tag should be 
	 * included in the filtered collection (as a potential EL expression).
	 * 
	 * @param addProperty the addproperty.
	 */
	public void setAddProperty(final String addProperty) {
		this.elAddProperty = addProperty;
	}

	// --------------------------------------------------------- Public Methods

	/**
	 * Process the start of this tag.
	 *
	 * @return the status of the start tag.
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		validateFilterAttributes();
		evaluateExpressions();

		// Acquire the select tag we are associated with
		final CTSelectTag selectTag = (CTSelectTag) pageContext.getAttribute("TODO"/*TODO Constants.SELECT_KEY*/);

		if (selectTag == null) {
			final JspException e = new JspException("TODO"/*TODO MESSAGES.getMessage("optionsCollectionTag.select")*/);
//			RequestUtils.saveException(pageContext, e);
			throw e;
		}

		acquireCodestableFromParent(selectTag);

		// Acquire the codestable containing our options
		final Collection collection = retrieveCodesTable(getPropertyValue(selectTag));

		if (collection == null) {
			final JspException e = new JspException("TODO"/*TODO MESSAGES.getMessage("optionsCollectionTag.collection")*/);
//			RequestUtils.saveException(pageContext, e);
			throw e;
		}

		final StringBuffer sb = new StringBuffer();

		if (addEmptyOption) {
			addOption(sb, emptyOptionText, "", selectTag.isMatched(""));
		}

		// Render the options
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			final Object bean = iter.next();

			// Get the label for this option
			final String stringLabel = retrievePropertyValueAsString(bean, BEAN_LABEL_PROPERTY);
			// Get the value for this option
			final String stringValue = retrievePropertyValueAsString(bean, BEAN_VALUE_PROPERTY);

			// Render this option
			addOption(sb, stringLabel, stringValue, selectTag.isMatched(stringValue));
		}

		// Render this element to our writer
//		ResponseUtils.write(pageContext, sb.toString());

		return SKIP_BODY;
	}

	/**
	 * Release any acquired resources.
	 */
	public void release() {
		super.release();

		codestable = null;
		elCodestable = null;
//		ctFilter = null; TODO: new way of handling this
		elFilterBean = null;
		elFilterFormProperty = null;
		comparator = null;
		elComparatorBean = null;
		elComparatorFormProperty = null;

		filterHtml = true;
		elFilterHtml = null;
		style = null;
		elStyle = null;
		styleClass = null;
		elStyleClass = null;
	}

	// ------------------------------------------------------ Protected Methods

	/**
	 * Validates the attributes for setting a {@link CodesTableManager.Filter} by checking that only one or none of
	 * elFilterBean or elFilterFormProperty is set.
	 * 
	 * @throws JspException of both elFilterBean and elFilterFormProperty is set.
	 */
	protected void validateFilterAttributes() throws JspException {
		if (null != elFilterBean && null != elFilterFormProperty) {
			final JspException jspe =
				new JspException(
					"You can specify either the Filter instance directly or "
						+ "the surrounding form's property which returns a Filter instance, but not both");
//			RequestUtils.saveException(pageContext, jspe);
			throw jspe;
		}

		if (null != elComparatorBean && null != elComparatorFormProperty) {
			final JspException jspe =
				new JspException(
					"You can specify either the Comparator instance directly or "
						+ "the surrounding form's property which returns a Comparator instance, but not both");
//			RequestUtils.saveException(pageContext, jspe);
			throw jspe;
		}
	}

	/**
	 * Evaluates the EL values in the properties specified by the user.
	 *
	 * @throws JspException if something went wrong evaluating.
	 */
	protected void evaluateExpressions() throws JspException {
		final ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);

		addProperty = eval.evaluateBoolean("addProperty", elAddProperty, addProperty);
		addEmptyOption = eval.evaluateBoolean("addEmptyOption", elAddEmptyOption, addEmptyOption);
		emptyOptionText = eval.evaluateString("emptyOptionText", elEmptyOptionText);
		codestable = eval.evaluateString("codestable", elCodestable);
//		ctFilter = evaluateFilterInstance(eval); TODO: new way of handling this
		comparator = evaluateComparatorInstance(eval);
		filterHtml = eval.evaluateBoolean("filterHtml", elFilterHtml, filterHtml);
		style = eval.evaluateString("style", elStyle);
		styleClass = eval.evaluateString("styleClass", elStyleClass);
	}

	/**
	 * Returns a CodesTableManager.Filter based on an EL expression.
	 * 
	 * @param eval the expression evaluator.
	 * @return the filter instance.
	 * @throws JspException a filter can not be created based on the EL expression.
	 */
/* TODO: new way of handling this
	private CodesTableManager.Filter evaluateFilterInstance(final ExpressionEvaluator eval) throws JspException {
		CodesTableManager.Filter ctFilter = null;

		if (null != elFilterBean) {
			ctFilter = (CodesTableManager.Filter) eval.evaluate("filterBean", elFilterBean, CodesTableManager.Filter.class);
		} else if (null != elFilterFormProperty) {
			final String formProperty = (String) eval.evaluate("filterFormProperty", elFilterFormProperty, String.class);
			ctFilter = (CodesTableManager.Filter) RequestUtils.lookup(pageContext, Constants.BEAN_KEY, formProperty, null);
		}

		return ctFilter;
	}
*/

	/**
	 * Returns a Comparator based on an EL expression.
	 *
	 * @param eval the expression evaluator.
	 * @return the filter instance.
	 * @throws JspException a filter can not be created based on the EL expression.
	 */
	private Comparator evaluateComparatorInstance(final ExpressionEvaluator eval) throws JspException {
		Comparator comparator = null;

		if (null != elComparatorBean) {
			comparator = (Comparator) eval.evaluate("filterBean", elComparatorBean, Comparator.class);
		} else if (null != elComparatorFormProperty) {
			final String formProperty = (String) eval.evaluate("comparatorFormProperty", elComparatorFormProperty, String.class);
//			comparator = (Comparator) RequestUtils.lookup(pageContext, Constants.BEAN_KEY, formProperty, null);
		}

		return comparator;
	}

	/**
	 * Add an option element to the specified StringBuffer based on the
	 * specified parameters.
	 *<p>
	 * Note that this tag specifically does not support the
	 * <code>styleId</code> tag attribute, which causes the HTML
	 * <code>id</code> attribute to be emitted.  This is because the HTML
	 * specification states that all "id" attributes in a document have to be
	 * unique.  This tag will likely generate more than one <code>option</code>
	 * element element, but it cannot use the same <code>id</code> value.  It's
	 * conceivable some sort of mechanism to supply an array of <code>id</code>
	 * values could be devised, but that doesn't seem to be worth the trouble.
	 *
	 * @param sb StringBuffer accumulating our results
	 * @param value Value to be returned to the server for this option
	 * @param label Value to be shown to the user for this option
	 * @param matched Should this value be marked as selected?
	 */
	protected void addOption(final StringBuffer sb, final String label, final String value, final boolean matched) {

		sb.append("<option value=\"");
		sb.append(value);
		sb.append("\"");

		if (matched) {
			sb.append(" selected=\"selected\"");
		}

		if (style != null) {
			sb.append(" style=\"");
			sb.append(style);
			sb.append("\"");
		}

		if (styleClass != null) {
			sb.append(" class=\"");
			sb.append(styleClass);
			sb.append("\"");
		}

		sb.append(">");

		if (filterHtml) {
//			sb.append(ResponseUtils.filter(label));
		} else {
			sb.append(label);
		}

		sb.append("</option>\r\n");
	}

	/**
	 * Use the codes table from the parent tag if it is a CTSelectTag.
	 *
	 * @param selectTag the parent select tag.
	 * @throws JspException if the codestable attribute is specified both in the parent tag and here.
	 */
	private void acquireCodestableFromParent(final CTSelectTag selectTag) throws JspException {
		if (selectTag instanceof CTSelectTag) {
			CTSelectTag ctSelectTag = (CTSelectTag) selectTag;

			if (!StringUtils.isBlank(ctSelectTag.codestable)) {
				if (StringUtils.isBlank(codestable)) {
					codestable = ctSelectTag.codestable;
				} else {
					final JspException e =
						new JspException(
							"Should not specify the codestable in both the select tag"
								+ " and the options collection tag, only in one of them");
//					RequestUtils.saveException(pageContext, e);
					throw e;
				}
			}
		}
	}

	/**
	 * Helper method for retrieving the collection of codes table items based on the class name name of the codes table
	 * (the package name <code>no.stelvio.common.domain</code> is appended). If a filter is specified, it will be
	 * used to filter out codes table items. If a comparator is specified, the items will be sorted with this.
	 *
	 * @param selectedPropertyValue the value of selected property.
	 * @return the collection of codes table items to show in the page.
	 * @throws JspException if no codes tables were found.
	 */
	private Collection retrieveCodesTable(String selectedPropertyValue) throws JspException {
		final Class doClass = CtTagSupport.retrieveClass(codestable);
		final CodesTableManager codesTableManager = CtTagSupport.retrieveCodesTableManager(pageContext);
		final Map ct = null;

/* TODO: new way of handling this
        ct = codesTableManager.getCodesTable(doClass);

        if (ct.isEmpty()) {
            throw new JspException("No codes table were found for " + doClass);
        }

        if (null == ctFilter) {
		}
		else {
			ct = codesTableManager.getFilteredCodesTable(doClass, ctFilter);

			if (addProperty) {
				CodesTableItem codesTableItem = codesTableManager.getCodesTableItem(doClass, selectedPropertyValue);

				// Could be that the codes table item is not found.
				if (null != codesTableItem) {
					ct.put(codesTableItem.getKode(), codesTableItem);
				}
			}
		}
*/
		return sort(ct.values());
	}

	/**
	 * Sorts the codes table items as specified by the <tag>comparator</tag> if it is not null.
	 *
	 * @param items the codes table items to be sorted.
	 * @return a sorted view of the codes table items.
	 * @see #comparator
	 */
	private Collection sort(final Collection items) {
		if (null == comparator) {
			return items;
		} else {
			List sortedItems = new ArrayList(items);
			Collections.sort(sortedItems, comparator);

			return sortedItems;
		}
	}

	/**
	 * Helper method for extracting the value from a property on a bean.
	 *
	 * @param bean the bean that has the property from which the value should be extracted.
	 * @param property the property with the value that should be extracted.
	 * @return the String representation of the extracted value.
	 * @throws JspException if something went wrong extracting the value
	 */
	private String retrievePropertyValueAsString(final Object bean, final String property) throws JspException {
		Object value;

		try {
			value = PropertyUtils.getProperty(bean, property);

			if (value == null) {
				value = "";
			}
		} catch (IllegalAccessException e) {
			final JspException jspe = new JspException("TODO"/*TODO MESSAGES.getMessage("getter.access", property, bean)*/);
//			RequestUtils.saveException(pageContext, jspe);
			throw jspe;
		} catch (InvocationTargetException e) {
			final Throwable t = e.getTargetException();
			final JspException jspe = new JspException("TODO"/*TODO MESSAGES.getMessage("getter.result", property, bean)*/);
//			RequestUtils.saveException(pageContext, jspe);
			throw jspe;
		} catch (NoSuchMethodException e) {
			final JspException jspe = new JspException("TODO"/*TODO MESSAGES.getMessage("getter.method", property, bean)*/);
//			RequestUtils.saveException(pageContext, jspe);
			throw jspe;
		}

		return value.toString();
	}

	/**
	 * Helper method to retrieve the selected property of the SelectTag.
	 * 
	 * Checking the value first, and then the property.
	 * 
	 * @param selectTag selectTag
	 * @return String property Value
	 * @throws JspException if property can't be retrieved.
	 */
	private String getPropertyValue(CTSelectTag selectTag) throws JspException {

		String name = selectTag.getName();
		String value = selectTag.getValue();
		String property = selectTag.getProperty();

		if (value == null) {
//			Object bean = RequestUtils.lookup(pageContext, name, null);
//			if (bean == null) {
//				JspException e = new JspException(MESSAGES.getMessage("getter.bean", name));
//				RequestUtils.saveException(pageContext, e);
//				throw e;
//			}

			try {
                Object bean = null;
                value = BeanUtils.getProperty(bean, property);

			} catch (IllegalAccessException e) {
//				RequestUtils.saveException(pageContext, e);
//				throw new JspException(MESSAGES.getMessage("getter.access", property, name));

			} catch (InvocationTargetException e) {
//				Throwable t = e.getTargetException();
//				RequestUtils.saveException(pageContext, t);
//				throw new JspException(MESSAGES.getMessage("getter.result", property, t.toString()));

			} catch (NoSuchMethodException e) {
//				RequestUtils.saveException(pageContext, e);
//				throw new JspException(MESSAGES.getMessage("getter.method", property, name));
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("selected value from SelectTag: " + value);
		}

		return value;
	}
}
