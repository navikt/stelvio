package no.stelvio.presentation.filter;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.executor.jsf.FlowExecutionHolderUtils;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.util.SequenceNumberGenerator;
import no.stelvio.presentation.util.RequestUtils;


/**
 * RequestContextFilter is an implementation of the <i>Intercepting Filter</i> pattern that
 * is responsible for constructing and destroying the <i>RequestContext</i> for each request
 * and response being processed. This filter should be the first filter in the chain.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: RequestContextFilter.java 2574 2005-10-20 08:04:22Z psa2920 $
 * @todo Rather make a listener? Or at least inherit from Spring's OncePerRequestFilter
 */
public class RequestContextFilter extends AbstractFilter {
	private static final String REQUEST_CONTEXT = "RequestContext";
	
	/**
	 * Performs the following processing steps:
	 * <ol>
	 * 	<li> Loads RequestContext from Session if possible </li>
	 * 	<li> Updates RequestContext with current screenId </li>
	 * 	<li> Updates RequestContext with current processId </li>
	 * 	<li> Updates RequestContext with current transactionId </li>
	 * 	<li> Delegates processing to the next filter or resource in the chain </li>
	 * 	<li> Stores RequestContext in Session if possible </li>
	 * 	<li> Resets RequestContext before next time</li>
	 * </ol>
	 * 
	 * {@inheritDoc}
	 * @see no.stelvio.web.filter.AbstractFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,  javax.servlet.FilterChain)
	 */
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		try {
			// Only import the request context from session if both the session
			// and a persisted context exists.
			HttpSession session = request.getSession(false);
			
			if (null != session) {
				Object context = session.getAttribute(REQUEST_CONTEXT);
				if (null != context) {
					RequestContext.importContext(context);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Session exits, but RequestContext was not persisted");
					}
				}
			}
	
			// Allways update the user, screen, module, process and transaction id
			RequestContext.setUserId(request.getRemoteUser());
			RequestContext.setScreenId(getScreenId());
			RequestContext.setModuleId(RequestContext.getScreenId());
			RequestContext.setProcessId(RequestUtils.getProcessId(request));
			RequestContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId("Transaction")));
			
			// Delegate processing to the next filter or resource in the chain
			chain.doFilter(request, response);
			
			// Session might have bean constructed, deleted or invalidated during
			// processing further down the chain, so check again
			session = request.getSession(false);
			if (null != session) {
				try {
					session.setAttribute(REQUEST_CONTEXT, RequestContext.exportContext());
				} catch (IllegalStateException ise) {
					if (log.isDebugEnabled()) {
						log.debug("Session was invalidated, could not persist RequestContext", ise);
					}
				}
			}
		}
		catch (Exception e) {
			throw new ServletException("An error occured while updating the RequestContext", e);
		}
		finally {
			// Always reset the RequestContext, just to be on the safe side
			RequestContext.remove();
		}
	}
	
	/**
	 * Method to retrieve the unique screen id. 
	 * @return the current flow filename and the state id of the viewstate, ex. my-flow-file:state-id
	 * @throws Exception 
	 */
	private String getScreenId() throws Exception {
		FlowSession session =  FlowExecutionHolderUtils.getFlowExecutionHolder(FacesContext.getCurrentInstance()).getFlowExecution().getActiveSession();
		if (session != null) {
			return session.getDefinition().getId()+":"+session.getState().getId();
		}
		
		return null;
	}
}