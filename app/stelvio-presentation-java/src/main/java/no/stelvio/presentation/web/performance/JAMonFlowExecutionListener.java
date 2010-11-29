package no.stelvio.presentation.web.performance;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.execution.EnterStateVetoException;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.FlowExecutionListener;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.View;
import org.springframework.webflow.mvc.servlet.MvcExternalContext;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

/**
 * FlowExecutionListener for JAMon performance measurements. Monitors the execution time between a requestSubmitted and
 * requestProcessed event. It would also be possible to monitor other events like stateEntering, stateEntered using a similar
 * approach.
 * 
 * @author personff564022aedd
 */
public class JAMonFlowExecutionListener implements FlowExecutionListener {

	/**
	 * Called when any client request is submitted to manipulate this flow execution. This call happens before request
	 * processing. Starts a JAMon monitor. The label of the monitor is in the example given by the URI and QueryString of the
	 * HttpServletRequest. Other attributes, like currentState or lastEvent, of RequestContext could be used in stead.
	 * 
	 * @param context
	 *            Spring Web Flow's context
	 */
	private boolean enablePerformanceMeasurement;

	/**
	 * Called when a client request starts processing.
	 * 
	 * @param context
	 *            Spring Web Flow's context
	 */
	public void requestSubmitted(RequestContext context) {

		if (enablePerformanceMeasurement && context != null) {
			MvcExternalContext externalContext = null;
			HttpServletRequest httpServletRequest = null;
			String uri = null;
			String flowId = null;
			if (!isFlowActive(context)) {
				flowId = context.getFlowExecutionContext().getDefinition().getId();
			} else {
				flowId = context.getActiveFlow().getId();
			}

			if (context != null) {
				externalContext = (MvcExternalContext) context.getExternalContext();
			}
			if (externalContext != null) {
				httpServletRequest = (HttpServletRequest) externalContext.getNativeRequest();
			}
			if (httpServletRequest != null) {
				uri = httpServletRequest.getRequestURI();
			}
			Monitor monitor = MonitorFactory.start(flowId + ":" + uri);
			context.getAttributes().put("jamon.monitor", monitor);
		}
	}

	/**
	 * Called when a client request has completed processing.
	 * 
	 * @param context
	 *            Spring Web Flow's context
	 */
	public void requestProcessed(RequestContext context) {

		if (enablePerformanceMeasurement && context != null) {
			Monitor monitor = (Monitor) context.getAttributes().get("jamon.monitor");

			if (monitor != null) {
				monitor.stop();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void eventSignaled(RequestContext context, Event event) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void exceptionThrown(RequestContext context, FlowExecutionException exception) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void paused(RequestContext context) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void resuming(RequestContext context) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionCreating(RequestContext context, FlowDefinition definition) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionEnded(RequestContext context, FlowSession session, String outcome, AttributeMap output) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionEnding(RequestContext context, FlowSession session, String outcome, MutableAttributeMap output) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionStarted(RequestContext context, FlowSession session) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void sessionStarting(RequestContext context, FlowSession session, MutableAttributeMap input) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateEntered(RequestContext context, StateDefinition previousState, StateDefinition state) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateEntering(RequestContext context, StateDefinition state) throws EnterStateVetoException {
	}

	/**
	 * {@inheritDoc}
	 */
	public void transitionExecuting(RequestContext context, TransitionDefinition transition) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void viewRendered(RequestContext context, View view, StateDefinition viewState) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void viewRendering(RequestContext context, View view, StateDefinition viewState) {
	}

	/**
	 * Gets the boolean value of the variable enabling or disabling performance measurement using JAMon.
	 * 
	 * @return the boolean value of the variable enabling or disabling performance measurement using JAMon
	 */
	public boolean isEnablePerformanceMeasurement() {
		return enablePerformanceMeasurement;
	}

	/**
	 * Sets the boolean value of the variable enabling or disabling performance measurement using JAMon.
	 * 
	 * @param enablePerformanceMeasurement
	 *            Boolean value enabling or disabling performance measurement using JAMon
	 */
	public void setEnablePerformanceMeasurement(boolean enablePerformanceMeasurement) {
		this.enablePerformanceMeasurement = enablePerformanceMeasurement;
	}

	/**
	 * Gets a double number value specifying if the monitor is active or not.
	 * 
	 * @deprecated returns 0 if measurement is disabled, 1 otherwise
	 * @return a double number value specifying if the monitor is active or not
	 */
	public double getMonitorState() {
		return enablePerformanceMeasurement ? 1.0 : 0.0;
		/*
		 * Monitor monitor = (Monitor)context.getAttributes().get("jamon.monitor");
		 * 
		 * if (monitor != null) { return monitor.getActive(); }
		 * 
		 * return 0;
		 */
	}

	/**
	 * Is flow active.
	 * 
	 * @param context
	 *            request context
	 * @return true if flow is active
	 */
	private boolean isFlowActive(RequestContext context) {
		FlowExecutionContext fec = context.getFlowExecutionContext();
		return fec.isActive();
	}

}
