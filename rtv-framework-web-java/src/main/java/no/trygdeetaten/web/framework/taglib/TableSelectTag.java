package no.trygdeetaten.web.framework.taglib;

import javax.servlet.jsp.JspException;
import org.apache.struts.taglib.html.SelectTag;

/**
 * Tag to display a selectbox (drop-down) within a display:table tag.
 * The <code>name</code> attribute holds the name of the
 * the object and <code>property</code> holds text field with the code 
 * value for the selectbox.
 * 
 * @author Christian Rømming, Accenture
 * @author person7553f5959484, Accenture
 * @author Espen Næss, Accenture
 * @version $Id: TableSelectTag.java 2214 2005-04-14 14:17:11Z skb2930 $
 */
public class TableSelectTag extends SelectTag {

	/**
	 * Overrides Struts' implementation to allow tag located inside displaytag.
	 * 
	 * {@inheritDoc}
	 * @see org.apache.struts.taglib.html.BaseHandlerTag#prepareIndex(java.lang.StringBuffer, java.lang.String)
	 */
	protected void prepareIndex(StringBuffer handlers, String name) throws JspException {

		if (!no.trygdeetaten.web.framework.taglib.support.TagSupport.prepareIndex(this, pageContext, handlers, name)) {
			super.prepareIndex(handlers, name);
		}
	}
}
