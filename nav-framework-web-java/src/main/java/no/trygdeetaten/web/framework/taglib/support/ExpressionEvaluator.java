package no.trygdeetaten.web.framework.taglib.support;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.nested.NestedPropertyHelper;
import org.apache.struts.taglib.nested.NestedReference;
import org.apache.struts.util.RequestUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/**
 * Helper class for evaluating EL.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id: ExpressionEvaluator.java 2040 2005-03-03 13:00:29Z psa2920 $
 */
public class ExpressionEvaluator {

	private final Log log = LogFactory.getLog(ExpressionEvaluator.class);

	private Tag tag;
	private PageContext pageContext;

	/**
	 * Constructs an ExpressionEvaluator for current tag and page context.
	 * 
	 * @param tag the tag.
	 * @param pageContext the page context.
	 */
	public ExpressionEvaluator(Tag tag, PageContext pageContext) {
		this.tag = tag;
		this.pageContext = pageContext;
	}

	/**
	 * Evaluate expression in attrValue. If we're inside a nested tag, also sets the nestedBean attribute in the
	 * pagecontext to the nested bean so it can be used in an EL in the JSP page.
	 *
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 * @return evaluate expression of attrValue, null if attrValue is null.
	 * @param returnClass class the returned object is instance of
	 * @throws JspException exception thrown by ExpressionEvaluatorManager
	 */
	public Object evaluate(String attrName, String attrValue, Class returnClass) throws JspException {
		Object result = null;

		// Are we inside a nested tag?
		NestedReference nr = (NestedReference) pageContext.getRequest().getAttribute(NestedPropertyHelper.NESTED_INCLUDES_KEY);

		if (null == nr) {
			if (log.isDebugEnabled()) {
				log.debug("NestedReference == null");
			}
		} else {
			Object property = RequestUtils.lookup(pageContext, nr.getBeanName(), nr.getNestedProperty(), null);
			pageContext.setAttribute("nestedBean", property, PageContext.REQUEST_SCOPE);

			if (log.isDebugEnabled()) {
				log.debug("NestedReference.beanName: " + nr.getBeanName());
				log.debug("               .nestedProperty: " + nr.getNestedProperty());
				log.debug("  gives the bean: " + property);
			}
		}

		if (attrValue != null) {
			result = ExpressionEvaluatorManager.evaluate(attrName, attrValue, returnClass, tag, pageContext);
		}

		if (log.isDebugEnabled()) {
			log.debug(attrValue + " evaluated to " + result + " for attribute " + attrName);
		}

		return result;
	}

	/**
	 * Evaluate expression in attrValue as a boolean.
	 *
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 * @param defaultValue the default value.
	 * @return evaluate expression of attrValue, false if attrValue is null.
	 * @throws javax.servlet.jsp.JspException exception thrown by ExpressionEvaluatorManager
	 */
	public boolean evaluateBoolean(String attrName, String attrValue, boolean defaultValue) throws JspException {

		boolean value = defaultValue;

		Boolean result = (Boolean) evaluate(attrName, attrValue, Boolean.class);

		if (result != null) {
			value = result.booleanValue();
		}

		return value;
	}

	/**
	 * Evaluate expression in attrValue as a String.
	 *
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 * @return evaluate expression of attrValue, null if attrValue is null.
	 * @throws javax.servlet.jsp.JspException exception thrown by ExpressionEvaluatorManager
	 */
	public String evaluateString(String attrName, String attrValue) throws JspException {
		return (String) evaluate(attrName, attrValue, String.class);
	}
}
