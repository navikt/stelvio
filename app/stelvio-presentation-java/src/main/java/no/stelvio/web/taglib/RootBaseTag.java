package no.stelvio.web.taglib;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * Override doStartTag to change the root base to the context path
 *
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 967 $ $Author: jla2920 $ $Date: 2004-08-03 13:18:17 +0200 (Tue, 03 Aug 2004) $
 * @todo we might need this in new framework, but then as a JSF-tag.
 */
public class RootBaseTag {

	/**
	 * Process the start of this tag.
	 *
	 * @return EVAL_BODY_INCLUDE
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String serverName = (this.server == null) ? request.getServerName() : this.server;

		// Get the basetag instead of the request URI        
		String baseTag = renderBaseElement(request.getScheme(), serverName, request.getServerPort(), request.getContextPath());

		JspWriter out = pageContext.getOut();
		try {
			out.write(baseTag);
		} catch (IOException e) {
			pageContext.setAttribute(Globals.EXCEPTION_KEY, e, PageContext.REQUEST_SCOPE);
			throw new JspException(messages.getMessage("common.io", e.toString()));
		}
		return EVAL_BODY_INCLUDE;
	}
}