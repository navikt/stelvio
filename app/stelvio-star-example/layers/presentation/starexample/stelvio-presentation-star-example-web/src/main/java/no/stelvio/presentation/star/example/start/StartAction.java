package no.stelvio.presentation.star.example.start;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import no.stelvio.domain.star.example.saksbehandling.Saksbehandler;
import no.stelvio.service.star.example.saksbehandling.SaksbehandlerServiceBi;


public class StartAction extends MultiAction {
	private static final Log log = LogFactory.getLog(StartAction.class);
	private SaksbehandlerServiceBi saksbehandlerService;

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

	public void setSaksbehandlerService(SaksbehandlerServiceBi saksbehandlerService) {
		this.saksbehandlerService = saksbehandlerService;
	}
}
