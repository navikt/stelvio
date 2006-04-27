package no.nav.web.framework.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.struts.taglib.html.Constants;
import org.apache.struts.taglib.html.FormTag;
import org.apache.struts.util.ResponseUtils;

import no.nav.common.framework.context.TransactionContext;

/**
 * Implementation of Struts' FormTag that holds state.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: StateAwareFormTag.java 2300 2005-06-01 15:43:19Z hza2920 $
 */
public class StateAwareFormTag extends FormTag {

	/**
	 * Render the beginning of this form as original implementation,
	 * but ads a hidden field for current state.
	 *  
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {

		// Look up the form bean name, scope, and type if necessary
		lookup();

		// Create an appropriate "form" element based on our parameters
		final StringBuffer results = new StringBuffer(renderFormStartElement());

		// Code to insert autocomplete="OFF" inside the <FORM ...> tag.
		int formIndex=results.indexOf("<form");
		if(formIndex==0){
			results.insert(formIndex+5," autocomplete=\"OFF\"");
		}
		else {
			// This is totally unexpected. 
			throw new JspException("Unexpected data when creating form-tag.");
		}
		
		// Add token
		results.append(renderToken());
		// RTV Specific state handling
		results.append(renderState());

		ResponseUtils.write(pageContext, results.toString());
		// Store this tag itself as a page attribute
		pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);

		initFormBean();

		return (EVAL_BODY_INCLUDE);

	}

	/**
	 * Generates a hidden input field with current state information.
	 * 
	 * @return A hidden input field containing the current state.
	 */
	protected String renderState() {

		StringBuffer results = new StringBuffer("<input type=\"hidden\" name=\"");
		results.append(no.nav.web.framework.constants.Constants.CURRENT_STATE);
		results.append("\" value=\"");
		results.append(TransactionContext.getState());
		results.append("\" />");
		return results.toString();
	}

}
