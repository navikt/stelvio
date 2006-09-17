package no.stelvio.web.framework.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.html.TextTag;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagExtraInfo;

/**
 * Custom text tag for use within the display:table tag.
 * Overrides the <code>doStartTag</code> method of the 
 * <code>BaseFieldTag</code> class. 
 * 
 * @author Christian Rømming, Accenture
 */
public class TableTextTag extends TextTag {

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
}