package no.stelvio.web.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;
import org.apache.struts.util.ModuleException;

import no.stelvio.common.error.LoggableException;

/**
 * Handler for <code>ApplicationException</code> exceptions. Set a flag in the context
 * which is read by the tag.
 *
 * @author Jonas Lindholm, Accenture
 * @version $Revision: 946 $ $Author: jla2920 $ $Date: 2004-07-28 16:30:51 +0200 (Wed, 28 Jul 2004) $
 */
public class FrameworkExceptionHandler extends ExceptionHandler {

    /**
     * Handle the exception. Set a flag in the request context if the exception it is an
     * <code>ApplicationException</code>.
     * Return the <code>ActionForward</code> instance (if any) returned by
     * the called <code>ExceptionHandler</code>.
     * <p/>
     * {@inheritDoc}
     */
    public ActionForward execute(
            Exception ex,
            ExceptionConfig ae,
            ActionMapping mapping,
            ActionForm formInstance,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException {

        ActionForward forward = null;
        ActionError error = null;
        String property = null;

        // Build the forward from the exception mapping if it exists
        // or from the form input
        if (ae.getPath() != null) {
            forward = new ActionForward(ae.getPath());
        } else {
            forward = mapping.getInputForward();
        }

        // Figure out the error
        if (ex instanceof ModuleException) {
            error = ((ModuleException) ex).getError();
            property = ((ModuleException) ex).getProperty();
        } else if (ex instanceof LoggableException) {
            error = new ActionError(ae.getKey(), /*ErrorHandler.getMessage(ex) TODO: look at new version*/ null);
            property = error.getKey();
        } else {
            error = new ActionError(ae.getKey(), ex.getMessage());
            property = error.getKey();
        }

        // Store the exception
        request.setAttribute(Globals.EXCEPTION_KEY, ex);
        storeException(request, property, error, forward, ae.getScope());

        return forward;
	}

}
