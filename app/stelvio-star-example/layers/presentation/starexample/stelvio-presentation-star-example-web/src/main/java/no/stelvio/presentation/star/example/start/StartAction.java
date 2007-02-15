package no.stelvio.presentation.star.example.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import no.nav.service.pensjon.saksbehandling.SaksbehandlerService;
import no.stelvio.star.example.saksbehandling.Saksbehandler;


public class StartAction extends MultiAction {
	private static final Log log = LogFactory.getLog(StartAction.class);
	private SaksbehandlerService saksbehandlerService;

	public Event search(RequestContext context) {
		StartForm form = (StartForm) context.getFlowScope().get("prsStartForm");
		log.debug("form.getSaksbehandlernr(): " + form.getSaksbehandlernr());
		log.debug("saksbehandlerService = " + saksbehandlerService);

		Saksbehandler saksbehandler = saksbehandlerService.readSaksbehandler(form.getSaksbehandlernr());
		log.debug("saksbehandler = " + saksbehandler);
		context.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);
		ExternalContextHolder.getExternalContext().getSessionMap().put("saksbehandlerDO", saksbehandler);

		return success();
	}

	public void setSaksbehandlerService(SaksbehandlerService saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}
}
