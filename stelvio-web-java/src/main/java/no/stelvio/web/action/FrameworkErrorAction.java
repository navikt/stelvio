package no.stelvio.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.MessageResources;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.ErrorHandler;
import no.stelvio.common.error.LoggableException;

/**
 * Handles errors before they are displayed on the global error page.
 * Exceptions must be configured in struts-config.xml to be handled
 * by this Action.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2757 $ $Author: skb2930 $ $Date: 2006-02-01 15:00:33 +0100 (Wed, 01 Feb 2006) $
 */
public class FrameworkErrorAction extends Action {

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Uses the ErrorHandler to handle the exception and prepares information pieces
	 * for display in the corresponding view.
	 * 
	 * {@inheritDoc}
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		FrameworkErrorForm errorForm = (FrameworkErrorForm) form;
		Exception e = (Exception) request.getAttribute(Globals.EXCEPTION_KEY);

		if (null != e) {
			if (log.isDebugEnabled()) {
				log.debug("Error found: " + e);
			}

			LoggableException le = (LoggableException) ErrorHandler.handleError(e);

			if (log.isDebugEnabled()) {
				log.debug("Transformed error through ErrorHandler: " + le);
			}

			ExceptionConfig exConfig = mapping.findException(e.getClass());

			if (null != exConfig) {
				if (log.isDebugEnabled()) {
					log.debug("ExceptionConfig found: " + exConfig);
				}

				MessageResources resources = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);

				if (null != resources) {
					String key = exConfig.getKey();

					if (null == key && log.isWarnEnabled()) {
						log.warn("Key not configured for exception:" + e.getClass().getName());
					}

					String message = resources.getMessage(key);

					if (null == message && log.isWarnEnabled()) {
						log.warn("Message not configured for key:" + key);
					}

					errorForm.setErrorMessage(message);
				}
			} else {
				errorForm.setErrorMessage(ErrorHandler.getMessage(e));
			}

			errorForm.setStacktrace(ErrorHandler.getStacktraceAsString(e));
			errorForm.setErrorCode(String.valueOf(le.getErrorCode()));
			errorForm.setErrorId(le.getErrorId());
			errorForm.setProcessId(le.getProcessId());
			errorForm.setScreenId(le.getScreenId());
			errorForm.setTransactionId(le.getTransactionId());
			errorForm.setUserId(le.getUserId());

		} else {

			if (log.isDebugEnabled()) {
				log.debug("There wasn't any errors stored in the request");
			}

			errorForm.setErrorMessage("En feil har oppstått.");
			errorForm.setStacktrace("Ukjent");
			errorForm.setErrorCode("Ukjent");
			errorForm.setErrorId("Ukjent");
			errorForm.setProcessId(RequestContext.getProcessId());
			errorForm.setScreenId(RequestContext.getScreenId());
			errorForm.setTransactionId(RequestContext.getTransactionId());
			errorForm.setUserId(RequestContext.getUserId());
		}

		if ("Load".equals(errorForm.getProcessId())) {
			errorForm.setProcessId("åpne skjermbilde");
		}

		if (log.isDebugEnabled()) {
			log.debug("Forward to view");
		}

		RequestContext.setProcessId("Load");
		return mapping.getInputForward();
	}
}