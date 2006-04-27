package no.nav.web.framework.taglib.support;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.displaytag.tags.TableTag;
import org.displaytag.tags.TableTagExtraInfo;
import org.apache.struts.taglib.html.BaseHandlerTag;

/**
 * Common support class for the tags.
 *
 * @author personf8e9850ed756
 * @version $Revision: 2211 $, $Date: 2005-04-14 14:33:59 +0200 (Thu, 14 Apr 2005) $
 */
public final class TagSupport {
	/**
	 * Helper method for preparing the index for tags nested inside TableTag.
	 *
	 * @param tag the tag to prepare the index for.
	 * @param handlers output will be appended her.
	 * @param name name of property to index.
	 * @return whether the indexed is prepared or not; is not prepared if the tag is not nested inside TableTag.
	 * @throws JspException
	 * @see BaseHandlerTag#prepareIndex(StringBuffer, String)
	 */
	public static boolean prepareIndex(BaseHandlerTag tag, PageContext pageContext, StringBuffer handlers, String name) throws JspException {
		// Is this tag nested inside TableTag from http://displaytag.sourceforge.net/
		TableTag tableTag = (TableTag) javax.servlet.jsp.tagext.TagSupport.findAncestorWithClass(tag, TableTag.class);

		if (null == tableTag) {
			return false;
		} else {
			Integer rowNumber = (Integer) pageContext.getAttribute(tableTag.getUid() + TableTagExtraInfo.ROWNUM_SUFFIX);

			if (null == rowNumber) {
				throw new JspException(
				        "RowNumber not found in PageContext using name " + tableTag.getUid() + TableTagExtraInfo.ROWNUM_SUFFIX);
			}

			if (null != name) {
				handlers.append(name);
			}

			handlers.append("[").append(rowNumber.intValue() - 1).append("]");

			if (null != name) {
				handlers.append(".");
			}

			return true;
		}
	}

}
