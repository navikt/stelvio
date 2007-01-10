package no.stelvio.presentation.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import no.stelvio.presentation.taglib.support.AmountSupport;
import no.stelvio.presentation.taglib.support.ExpressionEvaluator;

/**
 * Formats a number using the following rules:
 *
 * <ul>
 *   <li> Fractions are removed, that is: 10,40 becomes 10
 *   <li> Spaces are used as thousands separator, that is: 10 000 000
 *   <li> Negative numbers are shown with '-' in front, that is: -10 000
 * </ul>
 *
 * If the input parameter is a String it will be checked for the decimal signs ',' or '.' which will be removed together with
 * the fraction. The result is converted to an Integer.
 * If the input parameter is neither a String nor a Number, the input parameters <code>toString()</code> is returned.
 * All blank spaces will be converted to <code>&#38;nbsp;</code>.
 *
 * @author person5204c0b677af, Accenture
 * @author Stig Kleppe-J&oslash;rgensen, Accenture
 * @version $Revision: 2729 $ $Author: skb2930 $ $Date: 2006-01-03 13:53:40 +0100 (Tue, 03 Jan 2006) $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class AmountTag {
	private String elValue;
	private String elDefault;
	private Object evaluatedValue;

	/**
	 * Creates a new amount Tag
	 */
	public AmountTag() {
		init();
	}

	/**
	 * {@inheritDoc}
	 */
	public int doStartTag() throws JspException {
		evaluateExpressions();
		formatAmount();
//		return super.doStartTag();
        return 0;
    }

	/**
	 * {@inheritDoc}
	 */
	public void release() {
//		super.release();
		init();
	}

	/**
	 * Sets the value as an Expression Language string.
	 * 
	 * @param value the value.
	 */
	public void setValue(String value) {
		this.elValue = value;
	}

	/**
	 * Sets the default value as an Expression Language string.
	 * 
	 * @param def the default value.
	 */
	public void setDefault(String def) {
		this.elDefault = def;
	}

	/** Initializes the tag with default values. */
	private void init() {
		elValue = null;
		elDefault = null;
		evaluatedValue = null;
	}

	/**
	 * Evaluates expressions and formats value.
	 * 
	 * @throws JspException if expressions ccan't be evaluated.
	 */
	private void evaluateExpressions() throws JspException {
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator((Tag) this, null /*TODO pageContext*/);

//		def = expressionEvaluator.evaluateString("default", elDefault);
		evaluatedValue = expressionEvaluator.evaluate("value", elValue, Object.class);
	}

	/**
	 * Formats the amount.
	 */
	private void formatAmount() {
        String value = AmountSupport.formatAmount(evaluatedValue);
        // Should not escape output as then the &nbsp; as thousands separator will be escaped
        boolean escapeXml = false;
    }
}