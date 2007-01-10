package no.stelvio.presentation.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import no.stelvio.presentation.taglib.support.CtTagSupport;
import no.stelvio.presentation.taglib.support.ExpressionEvaluator;

/**
 * Custom tag for decoding a code using CodeTableManager.
 * 
 * @author Fredrik Dahl-J�rgensen, Accenture
 * @version $Id: CTDecodeTag.java 2038 2005-03-03 12:09:23Z psa2920 $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class CTDecodeTag {

	/** The class name of the codes table to show. */
	private String codestable = null;

	/** The class name of the codes table to show as a potential EL expression. */
	private String elCodestable = null;

	/** the el value */
	private String elValue = null;
    private Object value;

    /**
	 * Sets the name of the codes table.
	 * 
	 * @param codestable the codes table name.
	 */
	public void setCodestable(final String codestable) {
		this.elCodestable = codestable;
	}

	/**
	 * Sets the el value.
	 * 
	 * @param value the value, which might contain el.
	 */
	public void setValue(String value) {
		this.elValue = value;
	}

	/** Constructs a default instance of CTDecodeTag. */
	public CTDecodeTag() {
		init();
	}

	/**
	 * Evaluetes el and decodes the code.
	 *  
	 * {@inheritDoc}
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		evaluateExpressions();
        value = CtTagSupport.decode("value", codestable, null /*TODO pageContext*/);
        return 0;
    }

	/**
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		init();
	}

	/** Initializes the instance. */
	private void init() {
		value = null;
		elValue = null;
		codestable = null;
		elCodestable = null;
	}

	/**
	 * Evaluates the EL values in the properties specified by the user.
	 *
	 * @throws JspException if something went wrong evaluating.
	 */
	protected void evaluateExpressions() throws JspException {

		final ExpressionEvaluator eval = new ExpressionEvaluator((Tag) this, null /*TODO pageContext*/);
		codestable = eval.evaluateString("codestable", elCodestable);
		value = eval.evaluateString("value", elValue);
	}
}
