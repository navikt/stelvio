package no.stelvio.web.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.error.StelvioException;

/**
 * Handles errors before they are displayed on the global error page.
 * Exceptions must be configured in struts-config.xml to be handled
 * by this Action.
 * 
 * @author person7553f5959484, Accenture
 * @version $Revision: 2757 $ $Author: skb2930 $ $Date: 2006-02-01 15:00:33 +0100 (Wed, 01 Feb 2006) $
 * @todo we might need this in new framework.
 */
public class FrameworkErrorAction {

	/** Logger to be used for writing <i>TRACE</i> and <i>DEBUG</i> messages. */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * Uses the ErrorHandler (TODO: use new version) to handle the exception and prepares information pieces
	 * for display in the corresponding view.
	 * 
	 * {@inheritDoc}
	 */
	public String execute(
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		FrameworkErrorForm errorForm = new FrameworkErrorForm();
		Exception e = (Exception) request.getAttribute("TODO"/*TODO Globals.EXCEPTION_KEY*/);

		if (null != e) {
			if (log.isDebugEnabled()) {
				log.debug("Error found: " + e);
			}

			StelvioException le = (StelvioException) /*ErrorHandler.handleError(e) TODO: look at new version */ null;

			if (log.isDebugEnabled()) {
				log.debug("Transformed error through ErrorHandler: " + le);
			}

            errorForm.setErrorMessage(/*ErrorHandler.getMessage(e) TODO: look at new version*/ null);
			errorForm.setStacktrace(/*ErrorHandler.getStacktraceAsString(e)TODO: look at new version*/ null);
			errorForm.setErrorCode(String.valueOf(1/*le.getErrorCode()*/));
			errorForm.setErrorId("1"/*le.getErrorId()*/);
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
		return "ok";
	}
}