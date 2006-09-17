package no.stelvio.web.framework.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.strutsel.taglib.html.ELTextTag;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagExtraInfo;

import no.stelvio.web.framework.taglib.support.ExpressionEvaluator;

/**
 * Custom text tag for use within the display:table tag.
 * Overrides the <code>doStartTag</code> method of the 
 * <code>ELTextTag</code> class.
 * The tag writes inline text instead of form-element when readonly=true.
 * Added EL support plus override readonly attrib to write text only...
 * 
 * @author Alex Gray, Accenture
 * @version $Id: TableTextTagEL.java 2523 2005-10-07 06:17:58Z skb2930 $
 */
public class TableTextTagEL extends ELTextTag {

	/**
	 * Overrides Struts' implementation to allow tag located inside displaytag.
	 * 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.BaseHandlerTag#prepareIndex(StringBuffer, String)
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

		//Evaluate the readonly attrib. so JSTL expressions can be used
		ExpressionEvaluator exprEval = new ExpressionEvaluator(this, pageContext);

		setReadonly(exprEval.evaluateBoolean("readonly", getReadonlyExpr(), false));

		if (getReadonly()) {
			// Generate an HTML element
			StringBuffer results = new StringBuffer("<span class=\"readonly\">");
			results.append(calculateValue(exprEval));
			results.append("</span>");

			try {
				pageContext.getOut().write(results.toString());
			} catch (IOException e) {
				throw new JspException(messages.getMessage("common.io", e.toString()));
			}
		} else { //Readonly is false, so dont override
			super.doStartTag();
		}

		// Continue processing this page
		return EVAL_PAGE;
	}

	/**
	 * Gets a value from value property or if that is null, from bean name and property. 
	 * These properties are evaluated before being used.
	 *
	 * @param exprEval the expression evaluator to use for evaluating EL expressions.
	 * @return the value that should be shown for the field.
	 * @throws JspException if value cant be calculated.
	 */
	private String calculateValue(ExpressionEvaluator exprEval) throws JspException {
		value = exprEval.evaluateString("value", getValueExpr());

		if (null != value) {
			return ResponseUtils.filter(value);
		} else {
			property = exprEval.evaluateString("property", getPropertyExpr());
			name = exprEval.evaluateString("name", getNameExpr());

			if (null == property) {
				throw new JspException("Attribute 'property' must be specified when 'value' is not");
			}

			// if expr evaluated to null, then use the default which is the form bean
			if (null == name) {
				name = Constants.BEAN_KEY;
			}

			Object value = RequestUtils.lookup(pageContext, name, property, null);

			if (null == value) {
				value = "";
			}

			return ResponseUtils.filter(value.toString());
		}
	}
}