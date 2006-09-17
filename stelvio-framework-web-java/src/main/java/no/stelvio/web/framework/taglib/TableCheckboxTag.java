package no.stelvio.web.framework.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.taglib.html.CheckboxTag;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagExtraInfo;

/**
 * Tag to display a checkbox within a display:table tag.
 * The <code>name</code> attribute holds the name of the
 * boolean field in the object which determines whether or
 * not the checkbox is checked.
 * 
 * @author Christian Rømming, Accenture
 * @author person7553f5959484, Accenture
 * @version $Id: TableCheckboxTag.java 1583 2004-11-22 17:25:05Z psa2920 $
 */
public class TableCheckboxTag extends CheckboxTag {

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
