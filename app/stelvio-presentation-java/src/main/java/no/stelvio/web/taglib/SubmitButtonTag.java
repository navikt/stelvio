package no.stelvio.web.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * Adds the script necessary to use a <code>button</code> as a form submit button.
 * 
 * @author Christian Rømming, Accenture
 * @version $Revision: 1907 $ $Author: agb2970 $ $Date: 2005-01-26 16:04:27 +0100 (Wed, 26 Jan 2005) $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class SubmitButtonTag {

	/** The index of current form. */
	String formindex = null;

	String validateFnr = null;

	/**
	 * Returns the field to validate fnr
	 * 
	 * @return name of field to validate fnr
	 */
	public String getValidateFnr() {
		return validateFnr;
	}

	/**
	 * Sets the fieldname to validate
	 * 
	 * @param string The fieldname to validate
	 */
	public void setValidateFnr(String string) {
		validateFnr = string;
	}

	/**
	 * Returns the index of current form.
	 * 
	 * @return The index of current form.
	 */
	public String getFormindex() {
		return formindex;
	}

	/**
	 * Sets the index of current form.
	 * 
	 * @param formindex The index of current form.
	 */
	public void setFormindex(String formindex) {
		this.formindex = formindex;
	}

	/**
	 * Adds onclick script to the start tag.
	 * 
	 * @return EVAL_PAGE
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doEndTag() throws JspException {

		if (null == formindex) {
			formindex = "0";
		}

		// Generate an HTML element
		StringBuffer results = new StringBuffer();

		results.append("<button type=\"submit\" name=\"Btn").append(property).append("\"");
		if (accesskey != null) {
			results.append(" accesskey=\"");
			results.append(accesskey);
			results.append("\"");
		}
		if (tabindex != null) {
			results.append(" tabindex=\"");
			results.append(tabindex);
			results.append("\"");
		}

		// Add the onclick script
		if (property != null) {
			results.append(" onclick=\"");

			//If validateFnr is sat, add call for javascript validateFnr
			if (validateFnr != null) {
				results.append("return validateFnr('");
				results.append(validateFnr);
				results.append("', this");
				results.append(", '");
				results.append(property);
				results.append("')\"");
			} else {
				if (!formindex.equals("0")) {
					results.append("document.forms[");
					results.append(formindex);
					results.append("].Btn");
					results.append(property);
					results.append(".name='");
					results.append(property);
					results.append("'\"");

				} else {
					results.append("this.name='" + property + "'\"");
				}

			}
			results.append(prepareStyles());
			results.append(">");
		}
		results.append(text);
		results.append("</button>");

		// Print this value to our output writer
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(messages.getMessage("common.io", e.toString()));
		}

		// Continue processing this page
		return (EVAL_PAGE);
	}

}