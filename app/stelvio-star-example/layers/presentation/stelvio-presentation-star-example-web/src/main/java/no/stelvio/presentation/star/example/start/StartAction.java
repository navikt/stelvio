package no.stelvio.presentation.star.example.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceBi;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerRequest;
import no.stelvio.service.star.example.saksbehandling.to.SaksbehandlerResponse;

/**
 * Starts up by loading the saksbehandler chosen on the web page.
 */
public class StartAction extends MultiAction {
	private static final Log log = LogFactory.getLog(StartAction.class);
	private SaksbehandlerServiceBi saksbehandlerService;

	/**
	 * Searches for the saksbehandler chosen on the web page and sets it in the session.
	 * TODO: should not be set on session directly; use architecture component for this.
	 *
	 * @param context the RequestContext.
	 * @return success if everything works ok, throws exception otherwise.
	 */
	public Event search(RequestContext context) {
		StartForm form = (StartForm) context.getFlowScope().get("startForm");
		log.debug("form.getSaksbehandlernr(): " + form.getSaksbehandlernr());
		log.debug("saksbehandlerService = " + saksbehandlerService);

		SaksbehandlerResponse saksbehandlerResponse =
				saksbehandlerService.hentSaksbehandler(new SaksbehandlerRequest(form.getSaksbehandlernr()));
		log.debug("saksbehandler = " + saksbehandlerResponse.getSaksbehandler());
		ExternalContextHolder.getExternalContext().getSessionMap().
				put("saksbehandlerDO", saksbehandlerResponse.getSaksbehandler());

		return success();
	}

	public void setSaksbehandlerService(SaksbehandlerServiceBi saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}
}
