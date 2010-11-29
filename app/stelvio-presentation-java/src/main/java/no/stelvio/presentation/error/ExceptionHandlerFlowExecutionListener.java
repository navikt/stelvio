package no.stelvio.presentation.error;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;

/**
 * A listener of the webflow execution lifecycle allowing an exception handler to be added to each flow after it has been
 * created, but before it has been started. This way, no explicit configuration of the given exception handler is necessary,
 * using this listener is equivalent with adding the <code>&lt;exception-handler/&gt;</code> element to a flow.
 * <p>
 * Used in conjunction with the <code>ErrorPageExceptionHandler</code> this listener can be used to make all flows end up in a
 * generic error page when unhandled exceptions occur in the application.
 * <p>
 * The exception handler to add to the flow execution should be configured in the Spring configuration of the presentation layer
 * in the application.
 * <p>
 * 
 * @author person6045563b8dec (Accenture)
 * @author person9ea7150f0ee5 (Capgemini)
 * @since 1.0.5.4
 */
public class ExceptionHandlerFlowExecutionListener extends FlowExecutionListenerAdapter {

	/**
	 * Private logger instance for this class.
	 */
	private static final Log LOGGER = LogFactory.getLog(ExceptionHandlerFlowExecutionListener.class);

	/**
	 * The exception handler to add to the set of exception handlers registered for the flow.
	 */
	private FlowExecutionExceptionHandler exceptionHandler;

	/**
	 * Called after a new flow session has been created but before it starts. The exception handler is added to the current
	 * flows set of exception handlers.
	 * 
	 * @param context
	 *            the source of the event.
	 * @param session
	 *            the session that was created.
	 */
	@Override
	public void sessionStarted(RequestContext context, FlowSession session) {
		super.sessionStarted(context, session);

		if (this.exceptionHandler == null) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("Exception handler is null, make sure the exception handler for this listener "
						+ "is configured correctly");
			}
		}

		if (context.getFlowExecutionContext().isActive()) {
			FlowDefinition flowDef = context.getActiveFlow();
			Flow flow = (Flow) flowDef;

			if (!flow.getExceptionHandlerSet().contains(exceptionHandler)) {
				flow.getExceptionHandlerSet().add(exceptionHandler);
			}

		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.springframework.webflow.execution.FlowExecutionListenerAdapter#exceptionThrown(
	 *      org.springframework.webflow.execution.RequestContext,
	 *      org.springframework.webflow.execution.FlowExecutionException)
	 */
	@Override
	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {

		if (context.getFlowExecutionContext().isActive()) {
			FlowDefinition flowDef = context.getActiveFlow();
			Flow flow = (Flow) flowDef;

			if (!flow.getExceptionHandlerSet().contains(exceptionHandler)) {
				flow.getExceptionHandlerSet().add(exceptionHandler);
			}
		}

	}

	/**
	 * Sets the exception handler that is going to be added to the set of registered exception handlers for the current flow
	 * execution.
	 * 
	 * @param exceptionHandler
	 *            the exception handler to set.
	 */
	public void setExceptionHandler(FlowExecutionExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

}
