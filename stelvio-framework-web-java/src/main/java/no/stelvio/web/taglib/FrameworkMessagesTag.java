package no.stelvio.web.taglib;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.html.MessagesTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

/**
 * Description
 *
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 967 $ $Author: jla2920 $ $Date: 2004-08-03 13:18:17 +0200 (Tue, 03 Aug 2004) $
 */
public class FrameworkMessagesTag extends MessagesTag {

	/**
	 * Commons Logging instance.
	 */
	private static Log log = LogFactory.getLog(MessagesTag.class);

	/**
	 * Construct an iterator for the specified collection, and begin
	 * looping through the body once per element.
	 *
	 * @return EVAL_BODY_BUFFERED
	 * @exception JspException if a JSP exception has occurred 
	 */
	public int doStartTag() throws JspException {
		// Initialize for a new request.
		processed = false;

		// Were any messages specified?
		ActionMessages messages = null;

		// Make a local copy of the name attribute that we can modify.
		String name = this.name;

		if (message != null && "true".equalsIgnoreCase(message)) {
			name = Globals.MESSAGE_KEY;
		}

		try {
			messages = RequestUtils.getActionMessages(pageContext, name);
		} catch (JspException e) {
			RequestUtils.saveException(pageContext, e);
			throw e;
		}

		// Acquire the collection we are going to iterate over
		this.iterator = (property == null) ? messages.get() : messages.get(property);

		// Store the first value and evaluate, or skip the body if none
		if (!this.iterator.hasNext()) {
			return SKIP_BODY;
		}

		log.info("doStartTag - før iterator");

		ActionMessage report = (ActionMessage) this.iterator.next();
		String msg = RequestUtils.message(pageContext, bundle, locale, report.getKey(), report.getValues());

		// If the key hasn't a match in the resource bundle show the key instead.
		// The key is used as messageholder for ApplicationExceptions and AdminMessages.
		if (msg == null) {
			msg = report.getKey();
		}

		log.info("doStartTag - Setter pageContext");
		pageContext.setAttribute(id, msg);

		/*	From superclass not used  
		 	if (msg != null) {
					pageContext.setAttribute(id, msg);
				} else {
					pageContext.removeAttribute(id);
		
					// log missing key to ease debugging
					if (log.isDebugEnabled()) {
						log.debug(
							messageResources.getMessage(
								"messageTag.resources",
								report.getKey()));
					}
				}
		*/
		log.info("doStartTag - Etter pageContext");

		if (header != null && header.length() > 0) {
			String headerMessage = RequestUtils.message(pageContext, bundle, locale, header);

			if (headerMessage != null) {
				ResponseUtils.write(pageContext, headerMessage);
			}
		}

		// Set the processed variable to true so the
		// doEndTag() knows processing took place
		processed = true;

		log.info("doStartTag - Før return");
		return (EVAL_BODY_BUFFERED);
	}

	/**
	 * Make the next collection element available and loop, or
	 * finish the iterations if there are no more elements.
	 *
	 * @return EVAL_BODY_BUFFERED
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doAfterBody() throws JspException {

		log.info("doAfterBody - Start");
		// Render the output from this iteration to the output stream
		if (bodyContent != null) {
			ResponseUtils.writePrevious(pageContext, bodyContent.getString());
			bodyContent.clearBody();
		}

		// Decide whether to iterate or quit
		if (iterator.hasNext()) {
			ActionMessage report = (ActionMessage) iterator.next();
			String msg = RequestUtils.message(pageContext, bundle, locale, report.getKey(), report.getValues());

			// The key is used as messageholder for ApplicationExceptions and AdminMessages.
			if (msg == null) {
				msg = report.getKey();
			}

			pageContext.setAttribute(id, msg);

			log.info("doAfterBody - Før return");
			return (EVAL_BODY_BUFFERED);
		} else {
			return (SKIP_BODY);
		}

	}

	/**
	 * Clean up after processing this enumeration.
	 *
	 * @return EVAL_BODY_BUFFERED
	 * @exception JspException if a JSP exception has occurred
	 */
	public int doEndTag() throws JspException {
		if (processed && footer != null && footer.length() > 0) {
			String footerMessage = RequestUtils.message(pageContext, bundle, locale, footer);
			if (footerMessage != null) {
				// Print the results to our output writer
				ResponseUtils.write(pageContext, footerMessage);
			}
		}
		// Continue processing this page
		return (EVAL_PAGE);
	}
}
