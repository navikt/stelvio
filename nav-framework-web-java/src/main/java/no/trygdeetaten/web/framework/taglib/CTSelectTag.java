package no.trygdeetaten.web.framework.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.taglib.nested.NestedNameSupport;
import org.apache.struts.taglib.nested.NestedPropertyHelper;

import no.trygdeetaten.web.framework.taglib.support.CtTagSupport;
import no.trygdeetaten.web.framework.taglib.support.ExpressionEvaluator;
import no.trygdeetaten.web.framework.util.RequestUtils;

/**
 * Extension of the SelectTag by adding an attribute for determining if the field should be read-only or not.
 * When it is ready only, it will only output the value specified. If the codestable attribute is specified,
 * the decode of the value from the codestable will be output.
 *
 * @author personf8e9850ed756, Accenture
 * @version $Id: CTSelectTag.java 2523 2005-10-07 06:17:58Z skb2930 $
 */
public class CTSelectTag extends SelectTag implements NestedNameSupport {

	private static final Log LOG = LogFactory.getLog(CTSelectTag.class);

	private String originalName;
	private String originalProperty;

	/** The class name of the codes table to show. */
	String codestable = null;
	/** The class name of the codes table to show (as a potential EL expression). */
	String elCodestable = null;

	/**
	 * Sets the name of the codes table.
	 * 
	 * @param codestable the codes table name.
	 */
	public void setCodestable(final String codestable) {
		this.elCodestable = codestable;
	}

	/** Whether the field should be read only or not. */
	boolean readonly;
	/** Whether the field should be read only or not (as a potential EL expression). */
	String elReadonly = null;

	/**
	 * Setter for the property 'readonly' by mapping in the <code>CTSelectTagBeanInfo</code> class.
	 *
	 * @param readonly the value the readonly property is set to.
	 * @see CTSelectTagBeanInfo
	 */
	public void setElReadonly(String readonly) {
		this.elReadonly = readonly;
	}

	/** Constructs a default instance of CTSelectTag. */
	public CTSelectTag() {
		init();
	}

	/**
	 * If not read only, let the super class handle it all. Otherwise, write out the value or the decode of the value
	 * if the codestable attribute is set.
	 *
	 * @return SKIP_BODY if readonly is set, EVAL_BODY_TAG otherwise.
	 * @throws JspException if something went wrong outputting the value.
	 */
	public int doStartTag() throws JspException {
		evaluateExpressions();
		startNesting();

		if (readonly) {
			String out = calculateValue();

			if (LOG.isDebugEnabled()) {
				LOG.debug("calculated value: " + out);
			}

			if (!StringUtils.isBlank(codestable)) {
				out = CtTagSupport.decode(out, codestable, pageContext);

				if (LOG.isDebugEnabled()) {
					LOG.debug("decode from codestable (" + codestable + "): " + out);
				}
			}

			try {
				// OutSupport.out does not handle nulls
				if (null != out) {
					pageContext.getOut().write(out);
				}
			} catch (IOException ioe) {
				throw new JspException(ioe.getMessage(), ioe);
			}

			return SKIP_BODY;
		} else {
			return super.doStartTag();
		}
	}

	/** Start nesting. */
	private void startNesting() {
		// get the original properties
		originalName = getName();
		originalProperty = getProperty();

		// request
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		// set the properties
		NestedPropertyHelper.setNestedProperties(request, this);
	}

	/**
	 * If not read only, let the super class handle it all, else there is nothing do to,
	 * so just continue evaluating the page.
	 *  
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		int retVal;

		if (readonly) {
			retVal = Tag.EVAL_PAGE;
		} else {
			retVal = super.doEndTag();
		}

		endNesting();
		return retVal;
	}

	/** End nesting. */
	private void endNesting() {
		// reset the properties
		setName(originalName);
		setProperty(originalProperty);

	}

	/**
	 * Evaluates the EL values in the properties specified by the user and set the overriden attributes onto the
	 * attributes in the super class.
	 *
	 * @throws JspException if something went wrong evaluating.
	 */
	protected void evaluateExpressions() throws JspException {
		final ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);
		String evalVal;

		evalVal = eval.evaluateString("name", getName());

		if (null != evalVal) {
			setName(evalVal);
		}

		evalVal = eval.evaluateString("property", getProperty());

		if (null != evalVal) {
			setProperty(evalVal);
		}

		evalVal = eval.evaluateString("value", getValue());

		if (null != evalVal) {
			setValue(evalVal);
		}

		codestable = eval.evaluateString("codestable", elCodestable);
		readonly = eval.evaluateBoolean("readonly", elReadonly, false);
	}

	/**
	 * Overrides Struts' implementation so TableTag is handled too.
	 *
	 * @param handlers
	 * @param propertyName
	 * @throws JspException
	 */
	protected void prepareIndex(StringBuffer handlers, String propertyName) throws JspException {
		if (!no.trygdeetaten.web.framework.taglib.support.TagSupport.prepareIndex(this, pageContext, handlers, propertyName)) {
			super.prepareIndex(handlers, propertyName);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		super.release();
		init();
	}

	/**
	 * If the value is not set by the user, use the bean name and property to set it.
	 *
	 * @return the calcualted value.
	 * @throws JspException if something went wrong retrieving the property from the bean.
	 */
	private String calculateValue() throws JspException {
		if (null == getValue()) {
			return RequestUtils.retrievePropertyOnBeanAsString(pageContext, getName(), getProperty());
		} else {
			return getValue();
		}
	}

	/** Initialize tag with default values. */
	private void init() {
		readonly = false;
		elReadonly = null;
		codestable = null;
		elCodestable = null;
	}
}
