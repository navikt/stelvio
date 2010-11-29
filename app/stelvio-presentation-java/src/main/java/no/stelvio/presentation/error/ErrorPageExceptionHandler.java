package no.stelvio.presentation.error;

import no.stelvio.presentation.security.page.PageAccessDeniedException;
import no.stelvio.presentation.security.page.PageAuthenticationRequiredException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.execution.FlowExecutionException;

/**
 * Strategy for handling exceptions that occurs at runtime during the execution of a flow definition. The Spring Web Flow
 * execution engine triggers this exception handler for all flows that is configured to use this handler. The
 * <code>handles(...)</code> method is first invoked to check to see if this handler is the correct one to invoke for the type
 * of exception that has occured. Secondly the <code>handle(...)</code> method is invoked wich takes care of handling the
 * exception in the appropriate way.
 * 
 * The strategy of this exception handler is to send the user to a specified error page if this exception handler is configured
 * to handle the type of exception that has occured. The path to the error page to use, and which exceptions to handle should
 * both be configured in the Spring configuration of the presenation layer.
 * 
 * @author person6045563b8dec (Accenture)
 * @author person9ea7150f0ee5 (Capgemini)
 * @since 1.0.5.4
 */
public class ErrorPageExceptionHandler implements FlowExecutionExceptionHandler {

	private static final Log LOGGER = LogFactory.getLog(ErrorPageExceptionHandler.class);

	/** Attribute name which is used for storing the exception in session scope. */
	public static final String STATE_EXCEPTION_ATTRIBUTE = 
			"no.stelvio.presentation.error.ErrorPageExceptionHandler.STATE_EXCEPTION";

	/** The error page to send the user to when an exception has occured. */
	private String errorPage;

	/** The login page to send the user to when PageAuthenticationRequiredException is thrown. */
	private String loginPage;

	/** The access denied page to send the user to when PageAccessDeniedException is thrown. */
	private String accessDeniedPage;

	/**
	 * Handle the exception in the context of the current request. The exception that has occured is logged, and exposed in
	 * flashscope to make it available to other components. A new ApplicationView of the errorPage specified is then created and
	 * returned.
	 * 
	 * @param exception
	 *            the exception that occured
	 * @param ctx
	 *            the execution control context for this request 
	 *            
	 * return the selected error view that should be displayed (may be
	 *            null if the handler chooses not to select a view, in which case other exception handlers may be given a chance
	 *            to handle the exception)
	 */
	public void handle(FlowExecutionException exception, RequestControlContext ctx) {

		// Expose exception
		ctx.getExternalContext().getGlobalSessionMap().put(STATE_EXCEPTION_ATTRIBUTE, exception);

		MutableAttributeMap map = new LocalAttributeMap();

		if (ctx.getFlowExecutionContext().getKey() == null) {
			ctx.assignFlowExecutionKey();
		}

		if (exception.getCause() instanceof PageAuthenticationRequiredException) {
			if (loginPage == null && LOGGER.isErrorEnabled()) {
				LOGGER.error("'loginPage' must be configured.", exception);
			}
			ctx.getExternalContext().requestFlowDefinitionRedirect(loginPage, map);
		} else {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("", exception);
			}
			if (errorPage == null && LOGGER.isWarnEnabled()) {
				LOGGER.warn("An error page should be configured for this exception handler");
			}

			if (exception.getCause() instanceof PageAccessDeniedException) {
				if (accessDeniedPage != null) {
					ctx.getExternalContext().requestFlowDefinitionRedirect(accessDeniedPage, map);
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("'accessDeniedPage' is not configured. => goto error page instead.");
					}
					ctx.getExternalContext().requestFlowDefinitionRedirect(errorPage, map);
				}
			} else {
				ctx.getExternalContext().requestFlowDefinitionRedirect(errorPage, map);
			}
		}
	}

	/** {@inheritDoc} */
	public boolean canHandle(FlowExecutionException arg0) {
		return true;
	}

	/**
	 * Set the default error page to sent the user to when an exception occurs. The error page should be specified with full
	 * path relative to the applications context root.
	 * 
	 * @param errorPage
	 *            String representing the path to the default error page
	 */
	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}

	/**
	 * Sets the default login page to redirect to when A PageAuthenticationRequiredException is thrown.
	 * 
	 * @param loginPage
	 *            String representing the path to the login page
	 */
	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	/**
	 * Sets the default access denied page to redirect to when a PageAccessDeniedException is thrown.
	 * 
	 * @param accessDeniedPage
	 *            String representing the path to the access denied page
	 */
	public void setAccessDeniedPage(String accessDeniedPage) {
		this.accessDeniedPage = accessDeniedPage;
	}
}