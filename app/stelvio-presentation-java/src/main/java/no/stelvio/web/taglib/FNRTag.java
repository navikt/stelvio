package no.stelvio.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import no.stelvio.web.taglib.support.ExpressionEvaluator;

/**
 * Tag for displaying FNR. This will display on the follwoing format: ddmmyy xxxxx.
 * 
 * @author person356941106810, Accenture
 * @version $Id: FNRTag.java 2049 2005-03-03 14:28:52Z psa2920 $
 */
public class FNRTag {

	private static final int FNR_LEN = 11;

	private String fnrValue;
	private String fnrDefault;
	private String fnrEscapeXml;
    private String value;
    private String def;
    private boolean escapeXml;

    /**
	 * Creates a new FNR Tag
	 */
	public FNRTag() {
		init();
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {
		evaluateExpressions();
        return 0;
    }

	/** 
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#release()
	 */
	public void release() {
		init();
	}

	/**
	 * Sets the value.
	 * 
	 * @param val the value.
	 */
	public void setValue(String val) {
		this.fnrValue = val;
	}

	/**
	 * Sets the default value.
	 * 
	 * @param def the default value.
	 */
	public void setDefault(String def) {
		this.fnrDefault = def;
	}

	/**
	 * Sets the XML to be escaped.
	 * 
	 * @param ex the xml.
	 */
	public void setEscapeXml(String ex) {
		this.fnrEscapeXml = ex;
	}

	/** Initializes the tag with default values. */
	private void init() {
		fnrValue = null;
		fnrDefault = null;
		fnrEscapeXml = null;
	}

	/**
	 * Evaluates expressions.
	 * 
	 * @throws JspException if expressions ccan't be evaluated.
	 */
	private void evaluateExpressions() throws JspException {
		ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator((Tag) this, null /*TODO pageContext*/);

		String val = expressionEvaluator.evaluateString("value", fnrValue);
		value = val;

		if (val != null) {
			val = val.trim();

			if (FNR_LEN == val.length()) {
				value = val.substring(0, 6) + " " + val.substring(6);
			}
		}

		def = expressionEvaluator.evaluateString("default", fnrDefault);
		escapeXml = expressionEvaluator.evaluateBoolean("escapeXml", fnrEscapeXml, true);
	}
}
