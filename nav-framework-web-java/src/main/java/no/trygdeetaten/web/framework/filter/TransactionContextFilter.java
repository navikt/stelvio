package no.trygdeetaten.web.framework.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.trygdeetaten.common.framework.context.TransactionContext;
import no.trygdeetaten.common.framework.util.SequenceNumberGenerator;

import no.trygdeetaten.web.framework.constants.Constants;
import no.trygdeetaten.web.framework.util.RequestUtils;

/**
 * TransactionContextFilter is an implementation of the <i>Intercepting Filter</i> pattern that
 * is responsible for constructing and destroying the <i>TransactionContext</i> for each request
 * and response being processed. This filter should be the first filter in the chain.
 * 
 * @author person7553f5959484, Accenture
 * @version $Id: TransactionContextFilter.java 2574 2005-10-20 08:04:22Z psa2920 $
 */
public class TransactionContextFilter extends AbstractFilter {

	private static final String TRANSACTION_CONTEXT = "TransactionContext";

	/**
	 * Performs the following processing steps:
	 * <ol>
	 * 	<li> Loads TransactionContext from Session if possible </li>
	 * 	<li> Updates TransactionContext with current screenId </li>
	 * 	<li> Updates TransactionContext with current processId </li>
	 * 	<li> Updates TransactionContext with current transactionId </li>
	 * 	<li> Delegates processing to the next filter or resource in the chain </li>
	 * 	<li> Stores TransactionContext in Session if possible </li>
	 * 	<li> Resets TransactionContext before next time</li>
	 * </ol>
	 * 
	 * {@inheritDoc}
	 * @see no.trygdeetaten.web.framework.filter.AbstractFilter#doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		// Only import the transaction context from session if both the session
		// and a persisted context exists.
		HttpSession session = request.getSession(false);
		if (null != session) {
			Object context = session.getAttribute(TRANSACTION_CONTEXT);
			if (null != context) {
				TransactionContext.importContext(context);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Session exits, but TransactionContext was not persisted");
				}
			}
		}

		// Update user id if necessary
		if (null == TransactionContext.getUserId()) {
			String userId = request.getRemoteUser();
			// Support for anonymous access
			if (null == userId) {
				userId = "";
			}
			if (log.isDebugEnabled()) {
				log.debug("UserId not set! setting to " + userId);
			}
			TransactionContext.setUserId(userId.toUpperCase());
		}

		// Allways update the screen, module, process and transaction id
		TransactionContext.setScreenId(RequestUtils.getScreenId(request));
		TransactionContext.setModuleId(TransactionContext.getScreenId());
		TransactionContext.setProcessId(RequestUtils.getProcessId(request));
		TransactionContext.setTransactionId(String.valueOf(SequenceNumberGenerator.getNextId("Transaction")));

		// Check if current state is submitted with the request
		String state = request.getParameter(Constants.CURRENT_STATE);
		if (null == state) {
			// Apply default state
			TransactionContext.setState("normal");
		} else {
			// Apply desired state
			TransactionContext.setState(state);
		}

		// Delegate processing to the next filter or resource in the chain
		chain.doFilter(request, response);

		// Session might have bean constructed, deleted or invalidated during
		// processing further down the chain, so check again
		session = request.getSession(false);
		if (null != session) {
			try {
				session.setAttribute(TRANSACTION_CONTEXT, TransactionContext.exportContext());
			} catch (IllegalStateException ise) {
				if (log.isDebugEnabled()) {
					log.debug("Session was invalidated, could not persist TransactionContext", ise);
				}
			}
		}

		// Reset the TransactionContext allways, just to be on the safe side :)
		TransactionContext.remove();
	}
}
