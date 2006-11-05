package no.stelvio.web.taglib;

import java.io.IOException;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import no.stelvio.common.util.DateUtil;
import no.stelvio.web.taglib.support.DateSupport;

/**
 * Custom tag to easy date formatting.
 *
 * @author Jonas Lindholm, Accenture
 * @author person7553f5959484, Accenture
 * 
 * @version $Id: DateFormatTag.java 2713 2005-12-15 12:24:55Z skb2930 $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class DateFormatTag extends TagSupport {

	private String value; // stores EL-based property
	private Date date;
	private boolean showMonthAndYear = false;

	/** Constructs a default instance of DateFormatTag. */
	public DateFormatTag() {
		init();
	}

	/**
	 * Evaluates expression and chains to parent.
	 * 
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {

		// evaluate any expressions we were passed, once per invocation
		evaluateExpressions();

		// chain to the parent implementation
		return super.doStartTag();
	}

	/** {@inheritDoc} */
	public int doEndTag() throws JspException {

		try {
			String formatted;

			if (showMonthAndYear) {
				formatted = DateUtil.formatMonthlyPeriod(date);
			} else {
				formatted = DateUtil.format(date);
			}

			pageContext.getOut().print(DateSupport.blankToNonBreakSpace(formatted));
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	/**
	 * Releases any resources we may have (or inherit).
	 *  
	 * {@inheritDoc}
	 */
	public void release() {
		super.release();
		init();
	}

	/**
	 * Sets the EL-based value attribute.
	 *
	 * @param value the value.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Flag to decide whether month and year should be shown or not.
	 *
	 * @param flag the flag; true, false, yes or no in any case.
	 */
	public void setShowMonthAndYear(String flag) {
		if ("true".equalsIgnoreCase(flag) || "yes".equalsIgnoreCase(flag)) {
			showMonthAndYear = true;
		}
	}

	/** (re)initializes state (during release() or construction). */
	private void init() {
		// null implies "no expression"
		value = null;
	}

	/**
	 * Evaluates expressions as necessary.
	 *
	 * @throws JspException if expression evaluation fails.
	 */
	private void evaluateExpressions() throws JspException {

		// Note: we don't check for type mismatches here; we assume
		// the expression evaluator will return the expected type
		// (by virtue of knowledge we give it about what that type is).
		// A ClassCastException here is truly unexpected, so we let it
		// propagate up.

		// 'value' attribute (mandatory)
		date = (Date) ExpressionEvaluatorManager.evaluate("value", value, Date.class, this, pageContext);
	}
}