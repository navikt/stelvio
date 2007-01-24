package no.stelvio.presentation.jsf.event;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.executor.jsf.FlowExecutionHolderUtils;

import no.stelvio.common.context.RequestContext;
import no.stelvio.common.context.RequestContextHolder;
import no.stelvio.common.context.support.SimpleRequestContext;

/**
 * The RequestContextPhaseListener is executed during the request prosessing 
 * lifecycle. This listener updates the current RequestContext object. 
 * The RequestContext object is set through the 
 * <code>no.stelvio.presentation.context.RequestContextFilter</code>, because the
 * filter runs outside of the JSF and SWF context the screenId is not available at
 * the time the filter is run, this PhaseListener will set the screenId. 
 * 
 * @author person6045563b8dec, Accenture
 * @version $Id$
 *
 */
public class RequestContextPhaseListener implements PhaseListener {

	/**
	 * Auto-generated serialVersionUID 
	 */
	private static final long serialVersionUID = -2180489404440776289L;

	/**
	 * Execute operations after the phase has finished. When the phase is over the 
	 * RequestContext object will be updated with the current screen id. The screen id is
	 * fetched from the flow executions current session.
	 * @param event the PhaseEvent notifing that the phase has ended
	 */
	public void afterPhase(PhaseEvent event) {
		FlowSession session =  FlowExecutionHolderUtils.getFlowExecutionHolder(
				FacesContext.getCurrentInstance()).getFlowExecution().getActiveSession();
		if (session != null) {
			String screenId = session.getDefinition().getId()+":"+session.getState().getId();
			RequestContext requestContext = RequestContextHolder.currentRequestContext();
			RequestContext newRequestContext = new SimpleRequestContext.Builder(requestContext).screenId(screenId).build();
			RequestContextHolder.setRequestContext(newRequestContext);
		}
	}

	/**
	 * Handle a notification that the procession for the phase of the request
	 * processing lifecycle is about to begin
	 * 
	 * @param event PhaseEvent notifing that the phase is about to begin.
	 */
	public void beforePhase(PhaseEvent event) {
		// nothing to do
	}

	/**
	 * Returns the identifier of the request processing phaseduring which this listener is 
	 * interested in processing events. For this listener the phase identifier is 
	 * PhaseId.ANY_PHASE
	 * 
	 * @return PhaseId for this listener
	 */
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
