package no.stelvio.web.framework.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.strutsel.taglib.html.ELCheckboxTag;
import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagExtraInfo;

/**
 * Tag to display a checkbox within a display:table tag.
 * The <code>name</code> attribute holds the name of the
 * boolean field in the object which determines whether or
 * not the checkbox is checked.
 * 
 * @author Alex Gray, Accenture
 * @version $Id: TableCheckboxTagEL.java 2051 2005-03-03 14:50:57Z psa2920 $
 */
public class TableCheckboxTagEL extends ELCheckboxTag {

	/**
	 * Overrides Struts' implementation to allow tag located inside displaytag.
	 * 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.BaseHandlerTag#prepareIndex(java.lang.StringBuffer, java.lang.String)
	 */
	protected void prepareIndex(StringBuffer handlers, String name) throws JspException {

		// Is this tag nested inside TableTag from http://displaytag.sourceforge.net/
		TableTag tableTag = (TableTag) TagSupport.findAncestorWithClass(this, TableTag.class);
		if (null == tableTag) {
			// Use Struts' implementation if not nested inside TableTag
			super.prepareIndex(handlers, name);
		} else {

			Integer rowNumber = (Integer) pageContext.getAttribute(tableTag.getUid() + TableTagExtraInfo.ROWNUM_SUFFIX);
			if (null == rowNumber) {
				throw new JspException(
					"RowNumber not found in PageContext using name " + tableTag.getUid() + TableTagExtraInfo.ROWNUM_SUFFIX);
			}
			if (name != null) {
				handlers.append(name);
			}
			handlers.append("[").append(rowNumber.intValue() - 1).append("]");
			if (name != null) {
				handlers.append(".");
			}

		}
	}

	/** 
	 * {@inheritDoc}
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspException {

		value = (String) ExpressionUtil.evalNotNull("value", "value", super.getValueExpr(), String.class, this, pageContext);

		property =
			(String) ExpressionUtil.evalNotNull(
				"property",
				"property",
				super.getPropertyExpr(),
				String.class,
				this,
				pageContext);

		name = (String) ExpressionUtil.evalNotNull("name", "name", super.getNameExpr(), String.class, this, pageContext);

		String checked = "";
		if (value.equals("true") || value.equals("on") || value.equals("J") || value.equals("UK")) {
			checked = "true";
		}

		// Generate an HTML element
		StringBuffer results = new StringBuffer();

		results.append("<input type=\"checkbox\" ");
		results.append("name=\"");
		prepareIndex(results, name);
		results.append(property + "\" ");

		if (!checked.equals("")) {
			results.append("checked=\"true\" ");
		}

		if (!value.equals("")) {
			results.append("value=\"true\" ");
		}

		results.append("/>");

		// Print this value to our output writer
		JspWriter writer = pageContext.getOut();
		try {
			writer.print(results.toString());
		} catch (IOException e) {
			throw new JspException(messages.getMessage("common.io", e.toString()));
		}
		return (EVAL_PAGE);
	}
}